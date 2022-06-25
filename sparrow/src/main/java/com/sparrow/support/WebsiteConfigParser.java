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

import com.sparrow.constant.ConfigKeyDB;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.StringUtility;

import java.util.Map;

public class WebsiteConfigParser {

    public static com.sparrow.protocol.WebsiteConfig parse(Map<String, String> websiteConfigMap) {
        com.sparrow.protocol.WebsiteConfig websiteConfig = new com.sparrow.protocol.WebsiteConfig();
        if (websiteConfigMap != null && websiteConfigMap.size() > 0) {
            websiteConfig.setTitle(websiteConfigMap.get(ConfigKeyDB.WEBSITE_CONFIG_PARENT + "-"
                + ConfigKeyDB.WebsiteConfig.TITLE));
            websiteConfig.setDescription(websiteConfigMap.get(ConfigKeyDB.WEBSITE_CONFIG_PARENT
                + "-" + ConfigKeyDB.WebsiteConfig.DESCRIPTION));

            websiteConfig.setKeywords(websiteConfigMap.get(ConfigKeyDB.WEBSITE_CONFIG_PARENT
                + "-" + ConfigKeyDB.WebsiteConfig.KEYWORDS));
            websiteConfig.setContact(websiteConfigMap.get(ConfigKeyDB.WEBSITE_CONFIG_PARENT + "-"
                + ConfigKeyDB.WebsiteConfig.CONTACT));
            websiteConfig.setBanner(websiteConfigMap.get(ConfigKeyDB.WEBSITE_CONFIG_PARENT + "-"
                + ConfigKeyDB.WebsiteConfig.BANNER));

            websiteConfig.setIcp(websiteConfigMap.get(ConfigKeyDB.WEBSITE_CONFIG_PARENT + "-"
                + ConfigKeyDB.WebsiteConfig.ICP));

            websiteConfig.setBannerFlash(websiteConfigMap.get(ConfigKeyDB.WEBSITE_CONFIG_PARENT
                + "-" + ConfigKeyDB.WebsiteConfig.BANNER_FLASH));
            websiteConfig.setLogo(websiteConfigMap.get(ConfigKeyDB.WEBSITE_CONFIG_PARENT + "-"
                + ConfigKeyDB.WebsiteConfig.LOGO));
            return websiteConfig;
        }
        String configPrefix = "web_config_";
        websiteConfig.setTitle(ConfigUtility.getLanguageValue(configPrefix + ConfigKeyDB.WebsiteConfig.TITLE.toLowerCase()));
        websiteConfig.setDescription(ConfigUtility.getLanguageValue(configPrefix + ConfigKeyDB.WebsiteConfig.DESCRIPTION.toLowerCase()));
        websiteConfig.setKeywords(ConfigUtility.getLanguageValue(configPrefix + ConfigKeyDB.WebsiteConfig.KEYWORDS.toLowerCase()));
        websiteConfig.setContact(ConfigUtility.getLanguageValue(configPrefix + ConfigKeyDB.WebsiteConfig.CONTACT.toLowerCase()));
        String banner = ConfigUtility.getLanguageValue(configPrefix + ConfigKeyDB.WebsiteConfig.BANNER.toLowerCase());
        websiteConfig.setIcp(ConfigUtility.getLanguageValue(configPrefix + ConfigKeyDB.WebsiteConfig.ICP.toLowerCase()));
        String bannerFlash = ConfigUtility.getLanguageValue(configPrefix + StringUtility.humpToLower(ConfigKeyDB.WebsiteConfig.BANNER_FLASH));
        String logo = ConfigUtility.getLanguageValue(configPrefix + ConfigKeyDB.WebsiteConfig.LOGO.toLowerCase());
        websiteConfig.setLogo(StringUtility.replace(logo, Constant.REPLACE_MAP));
        websiteConfig.setBanner(StringUtility.replace(banner, Constant.REPLACE_MAP));
        websiteConfig.setBannerFlash(StringUtility.replace(bannerFlash, Constant.REPLACE_MAP));
        return websiteConfig;
    }

}
