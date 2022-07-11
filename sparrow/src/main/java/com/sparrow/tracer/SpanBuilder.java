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
package com.sparrow.tracer;

public interface SpanBuilder {
    /**
     * 由global span builder 构建child 因为构建时只需要确认parent,不需要确认follower, 所以asFollower 方法可以用asChild方法替代使用
     * <p>
     * 而且该方法有存在必要，否则name 构建时容易产生递归死循环
     *
     * @return
     */
    SpanBuilder asChild();

    /**
     * 构建span name
     *
     * @param operationName
     * @return
     */
    SpanBuilder name(String operationName);

    /**
     * 分类
     *
     * @param category
     * @return
     */
    SpanBuilder category(String category);

    /**
     * 设置span start 标记和时间 开始时可以判断parent是否结束，来设置child/follower
     * <p>
     * See http://opentracing.io/spec/#causal-span-references for more information about CHILD_OF references *public
     * static final String CHILD_OF = "child_of"; * See http://opentracing.io/spec/#causal-span-references for more
     * information about FOLLOWS_FROM references public static final String FOLLOWS_FROM = "follows_from";
     *
     * @return
     */
    Span start();
}