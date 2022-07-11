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

import java.util.Collection;
import java.util.List;

public interface Span {
    Tracer getTracer();

    void finish();

    String getName();

    String getId();

    Span parent();

    Span follower();

    List<Span> children();

    int duration();

    /**
     * Set a key:value tag on the Span.
     */
    Span setTag(String key, String value);

    void format(String format, Object... argArray);

    /**
     * Set a key:value tag on the Span.
     */
    Span setTag(String key);

    /**
     * Same as {@link #setTag(String, String)}, but for boolean values.
     */
    Span setTag(String key, boolean value);

    /**
     * Same as {@link #setTag(String, String)}, but for numeric values.
     */
    Span setTag(String key, Number value);

    Span setTag(String key, Collection t);
}
