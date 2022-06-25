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

package com.sparrow.core.algorithm.gouping;

public class Point<T extends Comparable> {
    private T point;
    private Boolean start;
    private Boolean end;

    public static <D extends Comparable> Point<D> copy(Point<D> point) {
        return new Point<D>(point.point, point.start, point.end);
    }

    public Point(T point) {
        this(point, null, null);
    }

    public Point(T point, Boolean start, Boolean end) {
        this.end = end;
        this.point = point;
        this.start = start;
    }

    public T getPoint() {
        return point;
    }

    public void setPoint(T point) {
        this.point = point;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(Boolean start) {
        this.start = start;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(Boolean end) {
        this.end = end;
    }
}
