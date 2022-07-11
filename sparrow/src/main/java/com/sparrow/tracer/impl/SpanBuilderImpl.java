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

import com.sparrow.tracer.Span;
import com.sparrow.tracer.SpanBuilder;
import com.sparrow.tracer.Tracer;

public class SpanBuilderImpl implements SpanBuilder {
    public SpanBuilderImpl() {
    }

    public SpanBuilderImpl(Tracer tracer) {
        this.tracer = (TracerImpl) tracer;
    }

    /**
     * span name
     */
    private String name;

    /**
     * 类别
     */
    private String category;
    /**
     * 当前span 的parent parent 为上一个span
     */
    private SpanImpl parent;
    /**
     * tracer 对象，全局
     */
    private TracerImpl tracer;

    @Override
    public SpanBuilder asChild() {
        /**
         * 由全局 builder 构建
         * 一个新span builder
         */
        SpanBuilderImpl spanBuilder = new SpanBuilderImpl();
        if (spanBuilder.parent == null) {
            spanBuilder.parent = (SpanImpl) tracer.cursor();
        }
        if (spanBuilder.parent == null) {
            spanBuilder.parent = (SpanImpl) tracer.root();
        }
        spanBuilder.tracer = (TracerImpl) spanBuilder.parent.getTracer();
        return spanBuilder;
    }

    @Override
    public SpanBuilder name(String operationName) {
        this.name = operationName;
        return this;
    }

    @Override
    public SpanBuilder category(String category) {
        this.category = category;
        return this;
    }

    @Override
    public Span start() {
        String name = this.name;
        if (this.category != null) {
            name = this.category + "#" + this.name;
        }
        SpanImpl span = new SpanImpl(this.tracer, System.currentTimeMillis(), name);
        //todo get ip:port
        span.setId("" + this.tracer.nextId());
        if (this.tracer.root() == null) {
            this.tracer.setRoot(span);
            this.tracer.setCursor(span);
        }
        tracer.setCursor(span);
        if (this.parent == null) {
            return span;
        }
        /**
         *See http://opentracing.io/spec/#causal-span-references for more information about CHILD_OF references
         *public static final String CHILD_OF = "child_of";
         * See http://opentracing.io/spec/#causal-span-references for more information about FOLLOWS_FROM references
         public static final String FOLLOWS_FROM = "follows_from";
         **/
        if (this.parent.isFinished()) {
            this.parent.setFollower(span);
            span.setFollower(true);
        } else {
            this.parent.addChild(span);
            span.setFollower(false);
        }
        span.setParent(this.parent);
        return span;
    }
}
