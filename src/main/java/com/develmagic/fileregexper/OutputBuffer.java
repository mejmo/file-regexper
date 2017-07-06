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

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * Created by mejmo on 5.7.2017.
 */
public class OutputBuffer {

    private HashMap<String, BufferedWriter> outputFiles = new HashMap<>();
    private Path outputPath;

    public OutputBuffer(Path outputPath) {
        this.outputPath = outputPath;
    }

    public void writeLine(String name, String line) {
        try {
            this.getStreamForRuleName(name).write(line + System.lineSeparator());
        } catch (IOException e) {
            throw new FileRegexperException("Cannot write a line to output file", e);
        }
    }

    private BufferedWriter getStreamForRuleName(String name) throws IOException {
        BufferedWriter bfw;
        if (outputFiles.get(name) == null) {
            synchronized (outputFiles) {
                try {
                    File destination = Paths.get(outputPath + File.separator + name).toFile();
                    destination.createNewFile();
                    bfw = new BufferedWriter(new FileWriter(destination));
                } catch (FileNotFoundException e) {
                    throw new FileRegexperException(e);
                }
                outputFiles.put(name, bfw);
            }
        }
        return outputFiles.get(name);
    }

    public void close() {
        outputFiles.values().stream().forEach(bfw -> {
            try {
                bfw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
