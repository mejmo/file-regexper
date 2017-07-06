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

public class Constants {

    //How many lines would be in one workset for one thread for multithreaded processing
    public static int WORKSET_SIZE_LINES = 10000;

    //Enable multithreading only for files greater than MULTITHREAD_ENABLED_FOR_FILES_LARGER
    public static int MULTITHREAD_ENABLED_FOR_FILES_LARGER = 10000000;

    //Get a count of virtual processors (including hyperthreading)
    public static final int CORE_COUNT = Runtime.getRuntime().availableProcessors();

    //How often to flush to the output file
    public static final int WRITER_BUFFER = 200 * 1024 * 1024;

}
