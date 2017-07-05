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

import java.util.regex.Pattern;

/**
 * Rule representing one line in config
 */
public class Rule {

    private String name;
    private Pattern pattern;

    public Rule(String configLine) {
        int i = configLine.indexOf(": ");

        if (i == -1)
            throw new FileRegexperException("com.develmagic.fileregexper.Rule not in correct format!");

        this.name = configLine.substring(0, i);
        this.pattern = Pattern.compile(configLine.substring(i+2));

    }

    public String getName() {
        return name;
    }

    public Pattern getPattern() {
        return pattern;
    }

}
