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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * Created by mejmo on 5.7.2017.
 */
public class OutputBuffer {

    private HashMap<String, FileOutputStream> outputFiles = new HashMap<>();
    private Path outputPath;

    public OutputBuffer(Path outputPath) {
        this.outputPath = outputPath;
    }

    public void writeLine(String name, String line) {
        try {
            this.getStreamForRuleName(name).write((line + System.lineSeparator()).getBytes());
        } catch (IOException e) {
            throw new FileRegexperException("Cannot write a line to output file", e);
        }
    }

    private FileOutputStream getStreamForRuleName(String name) throws IOException {
        FileOutputStream fileOutputStream;
        if (outputFiles.get(name) == null) {
            synchronized (outputFiles) {
                try {
                    File destination = Paths.get(outputPath + File.separator + name).toFile();
                    destination.createNewFile();
                    fileOutputStream = new FileOutputStream(destination);
                } catch (FileNotFoundException e) {
                    throw new FileRegexperException(e);
                }
                outputFiles.put(name, fileOutputStream);
            }
        }
        return outputFiles.get(name);
    }

}