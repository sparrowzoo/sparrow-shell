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

package com.sparrow.io.impl;

import com.sparrow.io.FileService;
import com.sparrow.utility.FileUtility;
import com.sparrow.utility.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JDKFileService implements FileService {
    private static Logger logger = LoggerFactory.getLogger(JDKFileService.class);

    @Override
    public boolean exist(String fullFilePath) throws IOException {
        return new File(fullFilePath).exists();
    }

    @Override
    public boolean mkdirs(String fullFilePath) throws IOException {
        return new File(fullFilePath).mkdirs();
    }

    @Override
    public OutputStream createOutputStream(String fullFilePath) throws IOException {
        return new FileOutputStream(new File(fullFilePath));
    }

    @Override
    public void createEmptyFile(String fullFilePath) throws FileNotFoundException {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File(fullFilePath));
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    logger.error("create empty file error", e);
                }
            }
        }
    }

    @Override
    public List<String> getFileNameList(String directory, final String extension) {
        File dir = new File(directory);
        List<String> fileList = new ArrayList<String>();
        if (!dir.exists()) {
            logger.debug("directory [{}] not exist", directory);
            return null;
        }
        File[] localFiles;

        if (!StringUtility.isNullOrEmpty(extension)) {
            localFiles = dir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(extension.toLowerCase());
                }
            });
        } else {
            localFiles = dir.listFiles();
        }
        if (localFiles == null || localFiles.length == 0) {
            logger.debug("directory [{}] files is null", directory);
            return fileList;
        }
        for (File file : localFiles) {
            fileList.add(file.getName());
        }
        return fileList;
    }

    @Override
    public List<String> getFileNameList(String directory) throws IOException {
        return getFileNameList(directory, null);
    }

    @Override
    public boolean isFile(String fullFilePath) throws IOException {
        return new File(fullFilePath).isFile();
    }

    @Override
    public void move(String fileName, String destDir) throws IOException {
        this.copy(fileName, destDir);
        this.delete(fileName);
    }

    @Override
    public boolean delete(String fileName) throws IOException {
        return new File(fileName).delete();
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public InputStream getInputStream(String fullFileName) throws IOException {
        try {
            return new FileInputStream(fullFileName);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public Long getFileLength(String filePath) throws IOException {
        try {
            return new File(filePath).length();
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public void copy(String srcName, String descPath) throws IOException {
        FileUtility.getInstance().copy(srcName, descPath);
    }
}
