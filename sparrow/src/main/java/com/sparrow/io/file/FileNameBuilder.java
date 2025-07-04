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

package com.sparrow.io.file;

import com.sparrow.utility.StringUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileNameBuilder {
    public FileNameBuilder(String prefix) {
        this.parts = new ArrayList<String>();
        this.parts.add(prefix);
    }

    private List<String> parts;
    private String fileName;
    private String extension;

    public FileNameBuilder joint(String source) {
        if (StringUtility.isNullOrEmpty(source)) {
            return this;
        }
        this.parts.add(source);
        return this;
    }

    public FileNameBuilder fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public FileNameBuilder extension(String extension) {
        this.extension = extension;
        return this;
    }

    public String build() {
        StringBuilder sb = new StringBuilder();
        String previous = File.separator;
        for (String part : this.parts) {
            if (StringUtility.isNullOrEmpty(part)) {
                continue;
            }
            if (!previous.endsWith(File.separator) && !part.startsWith(File.separator)) {
                sb.append(File.separator);
            }
            sb.append(part);
            previous = part;
        }

        if (!StringUtility.isNullOrEmpty(this.fileName)) {
            sb.append(File.separator).append(this.fileName);
        }
        if (!StringUtility.isNullOrEmpty(this.extension)) {
            if (!this.extension.startsWith(".")) {
                sb.append(".");
            }
            sb.append(this.extension);
        }
        return sb.toString();
    }
}
