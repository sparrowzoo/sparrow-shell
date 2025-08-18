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
package com.sparrow.facade.utility;

import com.sparrow.io.file.FileNameProperty;
import com.sparrow.utility.FileUtility;
import org.junit.Test;

/**
 * @author by harry
 */
public class FileTest {
    @Test
    public void fileExistInFile() {
        String fileName = "/lines_demo.properties";
        String line = "test5";
        FileUtility fileUtility = FileUtility.getInstance();
        while (true) {
            System.out.println(fileUtility.existLine(fileName, line));
        }
    }

    @Test
    public void fileName() {
        FileNameProperty fileNameProperty = FileUtility.getInstance().getFileNameProperty("http://www.baidu.com/a/b/c.jpg");
        fileNameProperty = FileUtility.getInstance().getFileNameProperty("d:\\a\\b\\c\\c.jsp");

        System.out.println(fileNameProperty.getExtensionWithoutDot());
        System.out.println(fileNameProperty);
    }
}
