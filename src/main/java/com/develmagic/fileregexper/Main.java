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

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class Main {

    public static void main(String[] args) {
        FileRegexper app = new FileRegexper();
        Arguments arguments = new Arguments();
        CmdLineParser cmdLineParser = new CmdLineParser(arguments);
        try {
            cmdLineParser.parseArgument(args);
            app.run(arguments.input, arguments.output, arguments.config);
        } catch (CmdLineException e) {
            System.err.println("Fileregexper - process files with regex rules");
            System.err.println(e.getMessage());
            cmdLineParser.printUsage(System.err);
        }
    }

    public static class Arguments {
        @Option(name = "-i", required = true, metaVar = "PATH", usage = "Input directory path")
        public String input;

        @Option(name = "-o", required = true, metaVar = "PATH", usage = "Output directory path")
        public String output;

        @Option(name = "-c", required = true, metaVar = "PATH", usage = "Configuration file")
        public String config;
    }

}
