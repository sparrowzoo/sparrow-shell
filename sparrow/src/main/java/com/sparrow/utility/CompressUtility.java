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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class CompressUtility {

    public static void zip(String fileName, OutputStream outputStream) {
        ZipOutputStream zipOutputStream = null;
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                throw new FileNotFoundException(fileName);
            }
            zipOutputStream = new ZipOutputStream(outputStream);
            ZipEntry entry = new ZipEntry(FileUtility.getInstance().getFileNameProperty(fileName).getFullFileName());
            zipOutputStream.putNextEntry(entry);
            FileUtility.getInstance().copy(new FileInputStream(file), zipOutputStream);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            if (zipOutputStream != null) {
                try {
                    zipOutputStream.close();
                } catch (IOException ignore) {
                }
            }
        }
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
        CompressUtility.unzip(inputStream, fileOutputStream);
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
            if (outputStream == null) {
                outputStream = new FileOutputStream(zipEntry.getName());
            }
            FileUtility.getInstance().copy(zipInputStream, outputStream);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            if (zipInputStream != null) {
                try {
                    zipInputStream.close();
                } catch (IOException ignore) {
                    ignore.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignore) {
                    ignore.printStackTrace();
                }
            }
        }

    }
}
