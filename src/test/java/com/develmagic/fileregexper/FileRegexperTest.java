package com.develmagic.fileregexper;

import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Consumer;

public class FileRegexperTest {

    /**
     * Singlethreaded test
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {

        new FileRegexper().run("src/test/resources/test1/input", "src/test/resources/test1/output", "src/test/resources/test1/rules.txt");

        assertThat(Files.list(Paths.get("src/test/resources/test1/output")).count(), is(3L));
        String[] files = new String[]{ "EXCELLENCE", "POSITION", "XC"};
        for (String str : files) {
            File file1 = new File("src/test/resources/test1/output/"+str);
            File file2 = new File("src/test/resources/test1/expected/"+str);
            assertTrue(Arrays.equals(Files.readAllBytes(file1.toPath()), Files.readAllBytes(file2.toPath())));
        }

    }

    /**
     * Multithreaded test
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {

        Constants.MULTITHREAD_ENABLED_FOR_FILES_LARGER = 1000;
        Constants.WORKSET_SIZE_LINES = 10;
        new FileRegexper().run("src/test/resources/test2/input", "src/test/resources/test2/output", "src/test/resources/test2/rules.txt");

        assertThat(Files.list(Paths.get("src/test/resources/test2/output")).count(), is(2L));
        String[] files = new String[]{ "TEST", "EIGHT"};
        for (String str : files) {
            File file1 = new File("src/test/resources/test2/output/"+str);
            File file2 = new File("src/test/resources/test2/expected/"+str);
            assertTrue(Arrays.equals(Files.readAllBytes(file1.toPath()), Files.readAllBytes(file2.toPath())));
        }

    }


}
