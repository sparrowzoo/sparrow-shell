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
package com.sparrow.distribution.config.repository.local.file;

import com.sparrow.distribution.config.repository.PropertiesContainer;

import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FilePropertiesContainer implements PropertiesContainer {
    private FileRepository fileRepository;

    public void setFileRepository(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    private Map<String, Properties> container = new ConcurrentHashMap<>();

    @Override
    public void put(String fileName) {
        this.container.put(fileName, this.fileRepository.read(fileName));
    }

    @Override
    public Properties read(String fileName) {
        return this.container.get(fileName);
    }

    @Override
    public void remove(String fileName) {
        this.container.remove(fileName);
    }

    @Override
    public Set<String> getAllFileName() {
        return this.container.keySet();
    }
}
