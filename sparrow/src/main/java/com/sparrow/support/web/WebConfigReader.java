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

package com.sparrow.support.web;

import java.util.List;

public interface WebConfigReader {

    List<String> getAutoMappingViewNames();

    Boolean getSupportTemplateEngine();

    List<String> getAjaxPattens();

    String getRootPath();

    String getPassport();

    String getLanguage();

    String getResource();

    String getPhysicalResource();

    String getResourceVersion();

    String getUpload();

    String getPhysicalUpload();

    String getDownload();

    String getPhysicalDownload();

    String getWaterMark();
    String getInternationalization();

    String getDefaultAvatar();

    String getLoginUrl();

    String getImageExtension();

    /**
     * 为兼容spring flash 功能实现
     *
     * @return
     */
    String getTemplateEngineSuffix();

    String getTemplateEnginePrefix();

    String getAdminPage();

    String getDefaultWelcomePage();

    String getErrorPage();
}
