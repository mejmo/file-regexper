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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RulesSet {

    public Collection<Rule> getRules() {
        return rules;
    }

    private Collection<Rule> rules;

    public RulesSet(Path config) {
        try (Stream<String> stream = Files.lines(config)) {
            rules = stream.map(line -> new Rule(line)).collect(Collectors.toList());
        } catch (IOException e) {
            throw new FileRegexperException("Cannot retrieve rules list", e);
        }
    }

    public Collection<RuleMatch> getRuleMatches(String line) {
        return rules.stream()
                .filter(rule -> rule.getPattern().matcher(line).matches())
                .map(rule -> new RuleMatch(rule, line))
                .collect(Collectors.toList());
    }


}