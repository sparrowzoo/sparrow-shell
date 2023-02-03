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
package com.sparrow.mvc.ui;

import com.sparrow.constant.Config;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.constant.OpenType;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.protocol.dto.SimpleItemDTO;
import com.sparrow.support.MenuService;
import com.sparrow.utility.CollectionsUtility;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.HtmlUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.tagext.TagSupport;
import java.util.List;

public class HorizontalMenu extends TagSupport {
    private static Logger logger = LoggerFactory.getLogger(HorizontalMenu.class);
    private String id;
    private String currentUrl;
    private String target = OpenType.SELF;
    private String indexUrl = "hot";

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    private MenuService menuService = ApplicationContext.getContainer().getBean(
        "menuService");

    public String getCurrentUrl() {
        if (this.getId() == null) {
            return this.currentUrl;
        }
        Object currentUrl = this.pageContext.getRequest().getAttribute(
            this.getId() + "." + "currentUrl");
        if (currentUrl != null) {
            this.currentUrl = currentUrl.toString();
        }
        if (this.currentUrl == null) {
            this.currentUrl = Symbol.EMPTY;
        }
        return this.currentUrl;
    }

    public void setCurrentUrl(String currentUrl) {
        this.currentUrl = currentUrl;
    }

    @Override
    public int doStartTag() {
        StringBuilder writeHTML = new StringBuilder();
        List<SimpleItemDTO> menus = null;
        try {
            menus = menuService.menu();
        } catch (BusinessException ignore) {
            logger.error("menu error {}", ignore);
            return TagSupport.SKIP_BODY;
        }
        StringBuilder menuList = new StringBuilder();
        menuList.append("<div class=\"list\">");

        writeHTML
            .append("<div class=\"index_menu\" onmouseover=\"function(e){$.event(e).cancelBubble();};\"");
        writeHTML.append(" id=\"" + this.getId() + "\">");
        writeHTML.append("<div class=\"menu\">");
        writeHTML.append("<ul>");
        try {
            for (SimpleItemDTO menu : menus) {
                String currentUrl = null;
                if (this.getCurrentUrl().trim().equals(this.indexUrl)) {
                    currentUrl = ConfigUtility.getValue(Config.ROOT_PATH);
                } else {
                    currentUrl = ConfigUtility.getValue(Config.ROOT_PATH) + "/"
                        + this.getCurrentUrl().trim();
                }
                if (menu.getUrl() != null
                    && currentUrl.equals(menu.getUrl().trim())) {
                    writeHTML.append("<li class=\"select\">");
                } else {
                    writeHTML.append("<li>");
                }
                writeHTML.append(HtmlUtility.assembleHyperLink(menu, this.getTarget()));
                writeHTML.append("</li>");

                menuList.append("<ul>");
                if (CollectionsUtility.isNullOrEmpty(menu.getChildren())) {
                    for (SimpleItemDTO subMenu : menu.getChildren()) {
                        menuList.append("<li>");
                        menuList.append(HtmlUtility.assembleHyperLink(subMenu, this.getTarget()));
                        menuList.append("</li>");
                    }
                }
                menuList.append("</ul>");
            }
            writeHTML.append("</ul>");
            writeHTML.append("</div>");

            menuList.append("</div>");
            writeHTML.append(menuList);
            writeHTML.append("</div>");

            this.pageContext.getOut().print(writeHTML.toString());
        } catch (Exception ex) {
            logger.error("io write ", ex);
        }
        return TagSupport.SKIP_BODY;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl;
    }

}
