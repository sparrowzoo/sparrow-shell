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

package com.sparrow.facade.compress;

import com.sparrow.support.EnvironmentSupport;
import com.sparrow.utility.CompressUtility;
import java.io.FileOutputStream;
import java.io.IOException;
import org.junit.Test;

/**
 * @author by harry
 */
public class CompressTest {
    @Test
    public void unzip() throws IOException {
        String path = EnvironmentSupport.getInstance().getApplicationSourcePath();
        String unzipFile = path + "/resources/logo_unzip.jpg";
        CompressUtility.unzip(path + "/resources/logo.zip");
        CompressUtility.unzip(path + "/resources/logo.zip",unzipFile);

        String readme = path + "/resources/readme_unzip.txt";
        CompressUtility.unzip(path + "/resources/readme.zip");
        CompressUtility.unzip(path + "/resources/readme.zip",readme);
    }


    @Test
    public void zip() throws IOException {
        String path = EnvironmentSupport.getInstance().getApplicationSourcePath();
        String logo = path + "/resources/logo.jpg";
        CompressUtility.zip(logo,new FileOutputStream(path+"/resources/logo.zip"));

        String readme = path + "/resources/README.md";
        CompressUtility.zip(readme,new FileOutputStream(path+"/resources/readme.zip"));
    }
}
