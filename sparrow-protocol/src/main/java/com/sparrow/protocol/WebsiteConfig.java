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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
