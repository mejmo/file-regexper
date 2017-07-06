package com.develmagic.fileregexper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;

/**
 * Created by mejmo on 5.7.2017.
 */
public class TestdataGenerator {

    public static void main(String[] args) throws IOException {

//        test1 - one big file

//        File input = new File("_test1/input");
//        input.mkdirs();
//
//        File output = new File("_test2/output");
//        output.mkdirs();
//
//        File template = new File("_template/1.txt");
//        byte[] b = Files.readAllBytes(template.toPath());
//
//        FileOutputStream fo = new FileOutputStream(input.getAbsolutePath()+"/1.txt");
//
//        long bytesWritten = 0;
//        while (bytesWritten < 2000000000) {
//            fo.write(b);
//            bytesWritten += b.length;
//        }
//        fo.close();

//        test2 - many small files

        File input = new File("_test2/input");
        input.mkdirs();

        File output = new File("_test2/output");
        output.mkdirs();

        File template = new File("_template/1.txt");
        byte[] b = Files.readAllBytes(template.toPath());

        for (int i = 1; i < 10000; i++) {
            FileOutputStream fo = new FileOutputStream(input.getAbsolutePath() + "/" + i + ".txt");
            fo.write(b);
            fo.close();
        }
        System.exit(0);

        // test3 - mixed small files and large files

//        File input = new File("_test3/input");
//        input.mkdirs();
//
//        File output = new File("_test3/output");
//        output.mkdirs();
//
//        File template = new File("_template/1.txt");
//        byte[] b = Files.readAllBytes(template.toPath());
//
//        long totalWritten = 0;
//
//        for (int i = 1; i < 100; i++) {
//            long bytesWritten = 0;
//            int c = new Random().nextInt(4000000);
//            FileOutputStream fo = new FileOutputStream(input.getAbsolutePath() + "/" + i + ".txt");
//            do {
//                fo.write(b);
//                bytesWritten += b.length;
//            } while (bytesWritten <= c);
//            fo.close();
//            totalWritten += bytesWritten;
//            System.out.println(totalWritten);
//        }
//
//        for (int i = 1; i < 5; i++) {
//            long bytesWritten = 0;
//            int c = 400000000;
//            FileOutputStream fo = new FileOutputStream(input.getAbsolutePath() + "/" + i + ".txt");
//            do {
//                fo.write(b);
//                bytesWritten += b.length;
//            } while (bytesWritten <= c);
//            fo.close();
//            totalWritten += bytesWritten;
//            System.out.println(totalWritten);
//        }


        //other test

//        FileOutputStream f = new FileOutputStream("_test/input/3.txt");
//        for (int i = 0; i < 100000; i++) {
//            f.write((i+" TEST\r\n").getBytes());
//        }
//        f.close();

    }

}
