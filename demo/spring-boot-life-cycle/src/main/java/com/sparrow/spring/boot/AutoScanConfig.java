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

package com.sparrow.spring.boot;

import com.sparrow.scan.CoderConfiguration;
import com.sparrow.scan.FileConfiguration;
import com.sparrow.scan.PassportConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
//@Import({CoderConfiguration.class, FileConfiguration.class, PassportConfiguration.class})
@CoderConfiguration
@FileConfiguration
@EnableSwagger2WebMvc
@ComponentScan(basePackages = {"com.sparrow.spring.boot.scan"})
//@ComponentScan(basePackages = {"com.sparrow.scan.coder"})
//@ComponentScan(basePackages = {"com.sparrow.scan.file"})
//@ComponentScan(basePackages = {"com.sparrow.scan.passport"})
public class AutoScanConfig {
}
