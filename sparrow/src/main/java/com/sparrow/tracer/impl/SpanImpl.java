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
package com.sparrow.tracer.impl;

import com.sparrow.core.spi.JsonFactory;
import com.sparrow.json.Json;
import com.sparrow.protocol.POJO;
import com.sparrow.tracer.Span;
import com.sparrow.tracer.TagPair;
import com.sparrow.tracer.Tracer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class SpanImpl implements Span {
    private Json json = JsonFactory.getProvider();

    public SpanImpl(Tracer tracer, Long startTime, String name) {
        this.tracer = (TracerImpl) tracer;
        this.startTime = startTime;
        this.name = name;
    }

    private TracerImpl tracer;
    private Span parent;
    private List<Span> children;
    private Span follower;
    private boolean finished;
    private boolean isFollower;
    private Long startTime;
    private String name;
    private String id;
    private Long endTime;

    private List<TagPair> tagPairs = new ArrayList<>(64);

    private void put(String key, Object value) {
        this.tagPairs.add(new TagPair(key, value));
    }

    public void setFollower(Span follower) {
        this.follower = follower;
    }

    public void setParent(Span parent) {
        this.parent = parent;
    }

    public void addChild(Span child) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        this.children.add(child);
    }

    public boolean isFinished() {
        return this.finished;
    }

    public void setFollower(boolean follower) {
        isFollower = follower;
    }

    @Override
    public void finish() {
        this.endTime = System.currentTimeMillis();
        this.finished = true;
        this.tracer.setCursor(this);
        if (this.duration() > tracer.getTimeoutThreshold()) {
            tracer.setTimeout();
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Span parent() {
        return this.parent;
    }

    @Override
    public Span follower() {
        return this.follower;
    }

    @Override
    public List<Span> children() {
        return this.children;
    }

    @Override
    public int duration() {
        return (int) (this.getEndTime() - this.getStartTime());
    }

    @Override
    public Span setTag(String key, String value) {
        this.put(key, value);
        return this;
    }

    @Override
    public void format(String format, Object... argArray) {
        String[] args = new String[argArray.length];
        int i = 0;
        for (Object arg : argArray) {
            if (arg instanceof Number) {
                args[i++] = String.valueOf(arg);
                continue;
            }
            if (arg instanceof String) {
                args[i++] = (String) arg;
                continue;
            }
            args[i++] = this.json.toString((POJO) arg);
        }
        this.put(String.format(format, args), "");
    }

    @Override
    public Span setTag(String key) {
        this.put(key, "");
        return this;
    }

    @Override
    public Span setTag(String key, boolean value) {
        this.put(key, value);
        return this;
    }

    @Override
    public Span setTag(String key, Number value) {
        this.put(key, value);
        return this;
    }

    @Override
    public Span setTag(String key, Collection list) {
        this.put(key, this.json.toString(list));
        return this;
    }

    @Override
    public Tracer getTracer() {
        return tracer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SpanImpl span = (SpanImpl) o;
        return Objects.equals(startTime, span.startTime) &&
            Objects.equals(name, span.name) &&
            Objects.equals(endTime, span.endTime) &&
            Objects.equals(children, span.children) &&
            Objects.equals(follower, span.follower);
    }

    public List<Span> getChildren() {
        return children;
    }

    public Span getFollower() {
        return follower;
    }

    public boolean isFollower() {
        return isFollower;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public List<TagPair> getTag() {
        return tagPairs;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, name, endTime, children, follower);
    }
}
