package com.sparrow.jdk.io;

import java.io.File;

public class FileTree {
    public static void main(String[] args) {
        File dir = new File("c:\\");
        outPutFileName(dir);
    }

    public static void outPutFileName(File dir) {
        for (File fileinfo : dir.listFiles()) {
            if (fileinfo.isFile()) {// 说明是文件类型
                System.out.println("文件路径" + fileinfo.getPath());
                System.out.println("文件名" + fileinfo.getName());
            } else {
                outPutFileName(fileinfo);
            }
        }
    }
}