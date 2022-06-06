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

import java.util.ArrayList;
import java.util.List;

/**
 * @author by harry
 */
public class TagListEntity {
    private List<TagListEntity> children=new ArrayList<TagListEntity>(16);
    private int indent;
    private TagListEntity parent;
    private String title;
    private String content;

    public List<TagListEntity> getChildren() {
        return children;
    }

    public void setChildren(List<TagListEntity> children) {
        this.children = children;
    }

    public int getIndent() {
        return indent;
    }

    public void setIndent(int indent) {
        this.indent = indent;
    }

    public TagListEntity getParent() {
        return parent;
    }

    public void setParent(TagListEntity parent) {
        this.parent = parent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void  addChild(TagListEntity tagListEntity){
        this.children.add(tagListEntity);
    }
}
