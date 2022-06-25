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
package com.sparrow.protocol.dto;

import com.sparrow.protocol.POJO;

public class ImageDTO implements POJO {
    private Boolean inner;
    private String imageId;
    private String remark;
    private String extension;
    private String imageMark;

    public ImageDTO(String imageId, String imageHtml, String extension, String remark, Boolean inner) {
        this.imageId = imageId;
        this.imageMark = imageHtml;
        this.extension = extension;
        this.remark = remark;
        this.inner = inner;
    }

    public String getImageId() {
        return imageId;
    }

    public String getImageMark() {
        return imageMark;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Boolean getInner() {
        return inner;
    }

    public void setInner(Boolean inner) {
        this.inner = inner;
    }
}
