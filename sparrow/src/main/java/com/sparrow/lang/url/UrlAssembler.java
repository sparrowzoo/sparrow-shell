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

package com.sparrow.lang.url;

import com.sparrow.constant.CacheNames;
import com.sparrow.container.Container;
import com.sparrow.core.cache.Cache;
import com.sparrow.core.cache.CacheRegistry;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.constant.Extension;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.support.web.WebConfigReader;
import com.sparrow.utility.StringUtility;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UrlAssembler {
    private String url;
    private Cache<String, String> actionKeyUrlCache = CacheRegistry.getInstance().getObject(CacheNames.ACTION_URL_CACHE);

    public UrlAssembler(String url) {
        this.url = url;
    }

    /**
     * url=action / =root/action
     *
     * @return prefix + action + suffix
     * /templates/action.html
     */
    public String assemble() {
        String actualUrl = this.actionKeyUrlCache.get(this.url);
        if (actualUrl != null) {
            return actualUrl;
        }
        if (StringUtility.isNullOrEmpty(this.url)) {
            return Symbol.EMPTY;
        }
        actualUrl = this.url;
        if (actualUrl.contains("?")) {
            actualUrl = actualUrl.substring(0, actualUrl.indexOf("?"));
        }

        Container container = ApplicationContext.getContainer();
        String rootPath = null;
        WebConfigReader configReader = null;
        if (container != null) {
            configReader = container.getBean(WebConfigReader.class);
            rootPath = configReader.getRootPath();
        }
        if (rootPath != null && actualUrl.startsWith(rootPath)) {
            actualUrl = actualUrl.substring(rootPath.length());
        }
        if (!actualUrl.startsWith(Symbol.SLASH)) {
            actualUrl = Symbol.SLASH + actualUrl;
        }
        String extension = Extension.HTML;
        String pagePrefix = Constant.TEMPLATE_ENGINE_PREFIX;
        if (configReader != null) {
            extension = configReader.getTemplateEngineSuffix();
            pagePrefix = configReader.getTemplateEnginePrefix();
        }
        if (!actualUrl.endsWith(extension)) {
            actualUrl = actualUrl + extension;
        }
        if (!actualUrl.startsWith(pagePrefix)) {
            actualUrl = pagePrefix + actualUrl;
        }
        this.actionKeyUrlCache.put(this.url, actualUrl);
        return actualUrl;
    }
}
