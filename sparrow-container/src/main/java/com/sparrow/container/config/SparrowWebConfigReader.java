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

package com.sparrow.container.config;

import com.sparrow.constant.Config;
import com.sparrow.container.ConfigReader;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.protocol.constant.Extension;
import com.sparrow.support.web.WebConfigReader;

import java.util.Arrays;
import java.util.List;

public class SparrowWebConfigReader implements WebConfigReader {
    ConfigReader configReader = ApplicationContext.getContainer().getBean(ConfigReader.class);

    @Override
    public List<String> getAutoMappingViewNames() {
        String autoMappingViewNames = configReader.getValue(Config.AUTO_MAPPING_VIEW_NAMES);
        String[] viewNames = autoMappingViewNames.split(",");
        return Arrays.asList(viewNames);
    }

    @Override
    public Boolean getSupportTemplateEngine() {
        return configReader.getBooleanValue(Config.SUPPORT_TEMPLATE_ENGINE);
    }

    @Override
    public String getTemplateEngineSuffix() {
        return configReader.getValue(Config.TEMPLATE_ENGINE_SUFFIX, Extension.HTML);
    }

    @Override
    public String getTemplateEnginePrefix() {
        return configReader.getValue(Config.TEMPLATE_ENGINE_PREFIX, "/template");
    }

    @Override
    public List<String> getAjaxPattens() {
        String ajaxPatterns = configReader.getValue(Config.AJAX_PATTERNS);
        String[] patterns = ajaxPatterns.split(",");
        return Arrays.asList(patterns);
    }

    @Override
    public String getRootPath() {
        return configReader.getValue(Config.ROOT_PATH);
    }

    @Override
    public String getLanguage() {
        return configReader.getValue(Config.LANGUAGE);
    }

    @Override
    public String getResource() {
        return configReader.getValue(Config.RESOURCE);
    }

    @Override
    public String getPhysicalResource() {
        return configReader.getValue(Config.RESOURCE_PHYSICAL_PATH);
    }

    @Override
    public String getResourceVersion() {
        return configReader.getValue(Config.RESOURCE_VERSION);
    }

    @Override
    public String getUpload() {
        return configReader.getValue(Config.UPLOAD);
    }

    @Override
    public String getPhysicalUpload() {
        return configReader.getValue(Config.UPLOAD_PHYSICAL_PATH);
    }

    public String getWaterMark() {
        return configReader.getValue(Config.WATER_MARK);
    }


    @Override
    public String getInternationalization() {
        return configReader.getValue(Config.INTERNATIONALIZATION);
    }

    @Override
    public String getDefaultAvatar() {
        return configReader.getValue(Config.DEFAULT_AVATAR);
    }

    public String getLoginUrl() {
        return configReader.getValue(Config.LOGIN_URL);
    }

    @Override
    public String getImageExtension() {
        return configReader.getValue(Config.IMAGE_EXTENSION);
    }


    @Override
    public String getAdminPage() {
        String adminPage = configReader.getValue(Config.DEFAULT_ADMIN_INDEX);
        if (!adminPage.endsWith("/")) {
            adminPage += "/";
        }
        return adminPage;
    }

    @Override
    public String getDefaultWelcomePage() {
        return configReader.getValue(Config.DEFAULT_WELCOME_INDEX);
    }

    @Override
    public String getErrorPage() {
        return configReader.getValue(Config.ERROR_URL);
    }

}
