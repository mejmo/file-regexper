/*
 * MIT License

 Copyright (c) 2017 Martin Formanko

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */
package com.develmagic.fileregexper;

import com.develmagic.fileregexper.exception.FileRegexperException;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Main logic
 */
public class FileRegexper {

    private Path inputPath;
    private Path outputPath;
    private Path configurationFilePath;
    private RulesSet rulesSet;
    private OutputBuffer outputBuffer;
    private static final Logger logger = Logger.getLogger(FileRegexper.class);

    private AtomicInteger[] stats = new AtomicInteger[3];

    private static final int SINGLETHREADED_COUNT = 0;
    private static final int MULTITHREADED_COUNT = 1;

    public void run(String input, String output, String config) {

        inputPath = Paths.get(input);
        outputPath = Paths.get(output);
        configurationFilePath = Paths.get(config);

        this.outputBuffer = new OutputBuffer(outputPath);
        this.rulesSet = new RulesSet(configurationFilePath);

        stats[SINGLETHREADED_COUNT] = new AtomicInteger(0);
        stats[MULTITHREADED_COUNT] = new AtomicInteger(0);

        long millis = Clock.systemUTC().millis();
        logger.info("Starting processing. ");
        this.startProcess();
        long result = Clock.systemUTC().millis() - millis;
        logger.info("Processing ended.");
        writeStats(result / 1000f);

    }

    /**
     * We will process the smaller files parallel but with one thread each file and the larger files
     * multithreaded.
     */
    private void startProcess() {
        try {
            Files.list(inputPath)
                    .filter(file -> file.toFile().length() < Constants.MULTITHREAD_ENABLED_FOR_FILES_LARGER)
                    .parallel()
                    .forEach(this::processFileSingleThread);
            Files.list(inputPath)
                    .filter(file -> file.toFile().length() >= Constants.MULTITHREAD_ENABLED_FOR_FILES_LARGER)
                    .forEach(this::processFileMultithreaded);
        } catch (IOException e) {
            throw new FileRegexperException("Cannot read from input folder", e);
        }
    }

    /**
     * Data is cut to smaller pieces (worksets) and these are processed by threads from ExecutorService.
     * @param file
     */
    private void processFileMultithreaded(Path file) {

        if (logger.isDebugEnabled())
            logger.debug("Processing file multi threaded: "+file.getFileName()+" Filesize: "+file.toFile().length());

        stats[MULTITHREADED_COUNT].incrementAndGet();
        try {

            FileInputStream fileInputStream = new FileInputStream(file.toFile());
            BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
            ExecutorService executorService = Executors.newFixedThreadPool(Constants.CORE_COUNT);

            String line = null;

            do {
                Workset[] worksets = new Workset[Constants.CORE_COUNT];

                for (int i = 0; i < Constants.CORE_COUNT; i++)
                    worksets[i] = new Workset(Constants.WORKSET_SIZE_LINES);

                for (int i = 0; i < Constants.CORE_COUNT; i++) {
                    while ((line = br.readLine()) != null) {
                        worksets[i].add(line);
                        if (worksets[i].size() >= Constants.WORKSET_SIZE_LINES)
                            break;
                    }
                    if (line == null)
                        break;
                }

                Future<List<WriteCommand>>[] results = new Future[Constants.CORE_COUNT];

                for (int i = 0; i < Constants.CORE_COUNT; i++) {
                    results[i] = executorService.submit(new WorksetProcessorTask(worksets[i]));
                }

                //We will wait for each thread to complete the task
                for (Future<List<WriteCommand>> result : results) {
                    for (WriteCommand writeCommand : result.get()) {
                        writeCommand.execute(this.outputBuffer);
                    }
                }

            } while (line != null);

            executorService.shutdown();

        } catch (IOException | ExecutionException e) {
            throw new FileRegexperException("Cannot process line", e);
        } catch (InterruptedException e) {
        }
    }

    /**
     * Line by line goes through the stream of lines
     * @param file
     */
    private void processFileSingleThread(Path file) {

        if (logger.isDebugEnabled())
            logger.info("Processing file single threaded: "+file.getFileName()+" Filesize: "+file.toFile().length());

        stats[SINGLETHREADED_COUNT].incrementAndGet();
        try {
            Files.lines(file).forEach(line -> this.processLine(line));
        } catch (IOException e) {
            throw new FileRegexperException("Cannot process line", e);
        }
    }

    /**
     * Gets a list of matched rules against the line and returns WriteCommand list for later execution
     * @param line
     * @return
     */
    private List<WriteCommand> processLineMultihreaded(String line) {
        return this.rulesSet.getRuleMatches(line).stream()
                .map(ruleMatch -> new WriteCommand(ruleMatch.getRule().getName(), line))
                .collect(Collectors.toList());
    }

    /**
     * Used for single threaded processing
     * @param line
     */
    private void processLine(String line) {
        this.rulesSet.getRuleMatches(line).stream()
                .forEach(ruleMatch -> this.outputBuffer.writeLine(ruleMatch.getRule().getName(), line));
    }

    /**
     * Task for executor service
     */
    public class WorksetProcessorTask implements Callable<List<WriteCommand>> {

        private Workset w;

        public WorksetProcessorTask(Workset w) {
            this.w = w;
        }

        @Override
        public List<WriteCommand> call() {
            return this.w.stream()
                    .map(line -> processLineMultihreaded(line))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
        }
    }

    private void writeStats(float duration) {
        logger.info("Singlethreaded files count: "+stats[SINGLETHREADED_COUNT].get());
        logger.info("Multithreaded files count:  "+stats[MULTITHREADED_COUNT].get());
        logger.info("Processing duration:        "+duration+" s");
    }

}