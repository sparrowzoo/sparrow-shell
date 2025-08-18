package com.sparrow.jdk;

import com.sparrow.utility.FileUtility;

import java.io.IOException;

public class ProcessBuilderTest {
    public static void main(String[] args) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("echo 1");
        Process process = processBuilder.start();
        System.out.println(FileUtility.getInstance().readLines(process.getInputStream()));
    }
}
