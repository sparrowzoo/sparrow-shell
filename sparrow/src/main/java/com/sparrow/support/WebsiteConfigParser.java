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

package com.sparrow.support;

import com.sparrow.container.ConfigReader;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.protocol.WebsiteConfig;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.utility.StringUtility;

public class WebsiteConfigParser {

    public static final class Key {
        public static final String TITLE = "TITLE";
        public static final String DESCRIPTION = "DESCRIPTION";
        public static final String BANNER = "BANNER";
        public static final String ICP = "ICP";
        public static final String BANNER_FLASH = "BANNER_FLASH";
        public static final String KEYWORDS = "KEYWORDS";
        public static final String LOGO = "LOGO";
        public static final String CONTACT = "CONTACT";
    }

    public static WebsiteConfig parse() {
        ConfigReader configReader = ApplicationContext.getContainer().getBean(ConfigReader.class);
        WebsiteConfig websiteConfig = new WebsiteConfig();
        String configPrefix = "web_config.";
        websiteConfig.setTitle(configReader.getI18nValue(configPrefix + Key.TITLE.toLowerCase()));
        websiteConfig.setDescription(configReader.getI18nValue(configPrefix + Key.DESCRIPTION.toLowerCase()));
        websiteConfig.setKeywords(configReader.getI18nValue(configPrefix + Key.KEYWORDS.toLowerCase()));
        websiteConfig.setContact(configReader.getI18nValue(configPrefix + Key.CONTACT.toLowerCase()));
        String banner = configReader.getI18nValue(configPrefix + Key.BANNER.toLowerCase());
        websiteConfig.setIcp(configReader.getI18nValue(configPrefix + Key.ICP.toLowerCase()));
        String bannerFlash = configReader.getI18nValue(configPrefix + StringUtility.humpToLower(Key.BANNER_FLASH));
        String logo = configReader.getI18nValue(configPrefix + Key.LOGO.toLowerCase());
        websiteConfig.setLogo(StringUtility.replace(logo, Constant.REPLACE_MAP));
        websiteConfig.setBanner(StringUtility.replace(banner, Constant.REPLACE_MAP));
        websiteConfig.setBannerFlash(StringUtility.replace(bannerFlash, Constant.REPLACE_MAP));
        return websiteConfig;
    }

}
