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
package com.sparrow.markdown.mark;

import java.util.List;

public class MarkEntity {
    private MarkEntity(MARK mark,Integer end) {
        this.mark = mark;
        this.end = end;
    }

    public static MarkEntity createCurrentMark(MARK mark, int end) {
        return new MarkEntity(mark, end);
    }

    private MARK mark;
    private int end;
    private String content;
    private String title;
    private String url;

    private String[] titleArray;
    private List<String[]> tdList;

    private List<TagListEntity> tagListEntities;

    private MarkEntity nextEntity;

    public MARK getMark() {
        return mark;
    }

    public void setMark(MARK mark) {
        this.mark = mark;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getTitleArray() {
        return titleArray;
    }

    public void setTitleArray(String[] titleArray) {
        this.titleArray = titleArray;
    }

    public List<String[]> getTdList() {
        return tdList;
    }

    public void setTdList(List<String[]> tdList) {
        this.tdList = tdList;
    }

    public List<TagListEntity> getTagListEntities() {
        return tagListEntities;
    }

    public void setTagListEntities(List<TagListEntity> tagListEntities) {
        this.tagListEntities = tagListEntities;
    }

    public MarkEntity getNextEntity() {
        return nextEntity;
    }

    public void setNextEntity(MarkEntity nextEntity) {
        this.nextEntity = nextEntity;
    }
}
