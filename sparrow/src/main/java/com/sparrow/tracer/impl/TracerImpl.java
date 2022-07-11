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

import com.sparrow.tracer.*;
import com.sparrow.utility.CollectionsUtility;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class TracerImpl implements Tracer {

    private boolean timeout = false;

    public void setTimeout() {
        this.timeout = true;
    }

    private LogFormatter logFormatter;

    private long timeoutThreshold = 300;

    public long getTimeoutThreshold() {
        return timeoutThreshold;
    }

    /**
     * cursor 当前span指针 thread local 需要remove 防止内存泄露，这里直接用map
     */
    private Map<Long, Span> cursor = new HashMap<>();
    /**
     * span id 生成器
     */
    private AtomicInteger nextId;
    /**
     * trace id
     */
    private String traceId;
    /**
     * 根span
     */
    private Span root;
    /**
     * 全局span builder 对象
     */
    private SpanBuilder spanBuilder;

    public Integer nextId() {
        return nextId.incrementAndGet();
    }

    public TracerImpl() {
        this(null, 300);
    }

    public TracerImpl(String traceId) {
        this(traceId, 300);
    }

    public TracerImpl(String traceId, long timeoutThreshold) {
        this.timeoutThreshold = timeoutThreshold;
        this.traceId = traceId;
        if (traceId == null) {
            this.traceId = UUID.randomUUID().toString();
        }
        this.nextId = new AtomicInteger(0);
        this.logFormatter = LogFormatterProvider.getLogFormatter();
        this.spanBuilder = new SpanBuilderImpl(this);
    }

    public void setRoot(Span root) {
        this.root = root;
    }

    @Override
    public Span root() {
        return this.root;
    }

    public void setCursor(Span current) {
        this.cursor.put(Thread.currentThread().getId(), current);
    }

    @Override
    public Span cursor() {
        return this.cursor.get(Thread.currentThread().getId());
    }

    @Override
    public boolean isTimeout() {
        return this.timeout;
    }

    @Override
    public String getId() {
        return this.traceId;
    }

    @Override
    public SpanBuilder spanBuilder() {
        return this.spanBuilder;
    }

    private void recursion(Span span, Map<Span, Integer> container, int depth) {
        if (span.follower() == null && CollectionsUtility.isNullOrEmpty(span.children())) {
            return;
        }
        if (!CollectionsUtility.isNullOrEmpty(span.children())) {
            for (Span child : span.children()) {
                container.put(child, depth + 1);
                recursion(child, container, depth + 1);
            }
        }
        if (span.follower() != null) {
            container.put(span.follower(), depth);
            recursion(span.follower(), container, depth);
        }
    }

    private String generate(int depth) {
        StringBuilder prefix = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            prefix.append("--");
        }
        return prefix.toString();
    }

    @Override
    public String walking() {
        Map<Span, Integer> spanDepthContainer = new LinkedHashMap<>();
        spanDepthContainer.put(root, 0);
        this.recursion(root, spanDepthContainer, 0);
        return this.logFormatter.format(this.traceId, spanDepthContainer);
    }

    @Override
    public void log(Logger logger, String parameters) {
        logger.info("tracer id:{} parameter:{} execute duration {}",
            this.getId(),
            parameters,
            this.walking());
    }
}
