/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sparrow.utility;

import com.sparrow.io.FolderFilter;
import com.sparrow.protocol.constant.magic.Symbol;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class CompressUtility {

    public static void zipDir(String directory, OutputStream outputStream, FolderFilter filter) {
        writeZip(outputStream, zipOutputStream -> {
            File dir = new File(directory);
            if (!dir.exists()) {
                throw new FileNotFoundException(directory);
            }
            if (!dir.isDirectory()) {
                throw new IllegalArgumentException(directory + " is not a directory");
            }
            String rootName = dir.getName();
            if (StringUtility.isNullOrEmpty(rootName)) {
                rootName = dir.getAbsolutePath();
            }
            compress(dir, rootName, zipOutputStream, filter);
        });
    }

    public static void zip(String fileName, OutputStream outputStream) {
        writeZip(outputStream, zipOutputStream -> {
            File file = new File(fileName);
            if (!file.exists()) {
                throw new FileNotFoundException(fileName);
            }
            String entryName = FileUtility.getInstance().getFileNameProperty(fileName).getFullFileName();
            writeEntry(zipOutputStream, entryName, new FileInputStream(file));
        });
    }

    /**
     * default unzip to current work directory
     *
     * @param zipFileName
     * @throws FileNotFoundException
     */
    public static void unzip(String zipFileName) throws FileNotFoundException {
        unzip(zipFileName, null);
    }

    public static void unzip(String zipFileName, String unzipFileName) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(zipFileName);
        FileOutputStream fileOutputStream = null;
        if (!StringUtility.isNullOrEmpty(unzipFileName)) {
            fileOutputStream = new FileOutputStream(unzipFileName);
        }
        unzip(inputStream, fileOutputStream);
    }

    /**
     * default unzip to current work directory
     *
     * @param inputStream
     */
    public static void unzip(InputStream inputStream) {
        unzip(inputStream, null);
    }

    /**
     * unzip
     *
     * @param inputStream
     * @param outputStream
     */
    public static void unzip(InputStream inputStream, OutputStream outputStream) {
        ZipInputStream zipInputStream = null;
        try {
            zipInputStream = new ZipInputStream(inputStream);
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            if (zipEntry == null) {
                return;
            }
            if (outputStream == null) {
                outputStream = new FileOutputStream(zipEntry.getName());
            }
            copy(zipInputStream, outputStream);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            close(zipInputStream);
            close(outputStream);
            close(inputStream);
        }
    }

    private static void writeZip(OutputStream outputStream, ZipOperation operation) {
        ZipOutputStream zipOutputStream = null;
        try {
            zipOutputStream = new ZipOutputStream(outputStream);
            operation.write(zipOutputStream);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            close(zipOutputStream);
        }
    }

    private static void compress(File file, String entryName, ZipOutputStream zipOutputStream, FolderFilter filter) throws IOException {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children == null) {
                return;
            }
            for (File child : children) {
                if (filter != null && filter.filter(child.getPath())) {
                    continue;
                }
                compress(child, entryName + Symbol.SLASH + child.getName(), zipOutputStream, filter);
            }
            return;
        }
        writeEntry(zipOutputStream, entryName, new FileInputStream(file));
    }

    private static void writeEntry(ZipOutputStream zipOutputStream, String entryName, InputStream inputStream) throws IOException {
        try {
            zipOutputStream.putNextEntry(new ZipEntry(entryName));
            copy(inputStream, zipOutputStream);
            zipOutputStream.closeEntry();
        } finally {
            close(inputStream);
        }
    }

    private static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
    }

    private static void close(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException ignore) {
        }
    }

    @FunctionalInterface
    private interface ZipOperation {
        void write(ZipOutputStream zipOutputStream) throws IOException;
    }
}
