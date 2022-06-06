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

package com.sparrow.protocol;

/**
 * @author harry
 */
public class WebsiteConfig implements POJO {
    private static final long serialVersionUID = -214177209049269222L;
    private String title;
    private String keywords;
    private String description;
    private String logo;
    private String banner;
    private String bannerFlash;
    private String icp;
    private String contact;

    public WebsiteConfig() {
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    @Override public String toString() {
        return "WebsiteConfig{" +
            "title='" + title + '\'' +
            ", keywords='" + keywords + '\'' +
            ", description='" + description + '\'' +
            ", logo='" + logo + '\'' +
            ", banner='" + banner + '\'' +
            ", bannerFlash='" + bannerFlash + '\'' +
            ", icp='" + icp + '\'' +
            ", contact='" + contact + '\'' +
            '}';
    }

    public String getBannerFlash() {
        return bannerFlash;
    }

    public void setBannerFlash(String bannerFlash) {
        this.bannerFlash = bannerFlash;
    }

    public String getIcp() {
        return icp;
    }

    public void setIcp(String icp) {
        this.icp = icp;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

}
