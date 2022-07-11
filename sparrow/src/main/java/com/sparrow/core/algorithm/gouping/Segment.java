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

public class Segment<T extends Comparable> {
    private Point<T> start;
    private Point<T> end;

    public Segment(T start, T end) {
        if (end.compareTo(start) < 0) {
            throw new IllegalArgumentException("this.end<this.start");
        }
        this.start = new Point<T>(start);
        this.end = new Point<T>(end);
    }

    public Segment(Point<T> start, Point<T> end) {
        if (end.getPoint().compareTo(start.getPoint()) < 0) {
            throw new IllegalArgumentException("end=" + end.getPoint() + "< start=" + start.getPoint());
        }
        this.start = start;
        this.end = end;
    }

    public Point<T> getStart() {
        return start;
    }

    public void setStart(Point<T> start) {
        this.start = start;
    }

    public Point<T> getEnd() {
        return end;
    }

    public void setEnd(Point<T> end) {
        this.end = end;
    }

    public boolean equals(Segment<T> segment) {
        if (this.start.getPoint().compareTo(segment.start.getPoint()) != 0) {
            return false;
        }
        return this.end.getPoint().compareTo(segment.end.getPoint()) == 0;
    }

    public Segment<T> intersection(Segment<T> segment) {
        if (segment.getEnd().getPoint().compareTo(segment.getStart().getPoint()) < 0) {
            throw new IllegalArgumentException("segment.end < segment.start");
        }
        //取大的起始节点
        Point<T> start = this.start.getPoint().compareTo(segment.start.getPoint()) > 0 ? this.start : segment.start;
        //取小的截止节点
        Point<T> end = this.end.getPoint().compareTo(segment.end.getPoint()) < 0 ? this.end : segment.end;

        if (start.getPoint().compareTo(end.getPoint()) <= 0) {
            return new Segment(start, end);
        }
        return null;
    }

    public Segment union(Segment<T> segment) {
        if (segment.getEnd().getPoint().compareTo(segment.getStart().getPoint()) < 0) {
            throw new IllegalArgumentException("segment.end < segment.start");
        }
        //取小的起始节点
        Point<T> start = this.start.getPoint().compareTo(segment.start.getPoint()) < 0 ? this.start : segment.start;
        //取大的截止节点
        Point<T> end = this.end.getPoint().compareTo(segment.end.getPoint()) > 0 ? this.end : segment.end;

        if (start.getPoint().compareTo(end.getPoint()) <= 0) {
            return new Segment<T>(start, end);
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Segment<?> segment = (Segment<?>) o;
        return start.equals(segment.start) && end.equals(segment.end);
    }

    @Override
    public int hashCode() {
        int result = start.hashCode();
        result = 31 * result + end.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Segment{" +
            "start=" + start.getPoint() +
            ", end=" + end.getPoint() +
            '}';
    }
}
