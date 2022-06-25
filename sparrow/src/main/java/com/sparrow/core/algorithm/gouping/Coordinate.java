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

import java.util.*;

public class Coordinate<E extends Segment<D>, D extends Comparable> {
    public Coordinate(List<E> dataList) {
        this.dataList = dataList;
    }

    private List<E> dataList = new ArrayList<E>();

    protected List<Segment<D>> segments = new ArrayList<Segment<D>>();

    protected List<Point<D>> coordinate = new ArrayList<Point<D>>();

    public List<Point<D>> getCoordinate() {
        return coordinate;
    }

    public void draw() {
        Map<Comparable, Point<D>> coordinate = new TreeMap<Comparable, Point<D>>();
        for (E segment : this.dataList) {
            Point<D> point = coordinate.get(segment.getStart().getPoint());
            if (point == null) {
                point = new Point<D>(segment.getStart().getPoint(), true, null);
                coordinate.put(segment.getStart().getPoint(), point);
            }

            point = coordinate.get(segment.getEnd().getPoint());
            if (point == null) {
                point = new Point<D>(segment.getEnd().getPoint(), null, true);
                coordinate.put(segment.getEnd().getPoint(), point);
            }
        }
        this.coordinate = new ArrayList<Point<D>>(coordinate.values());
    }

    public void section() {
        for (int i = 0; i < this.coordinate.size() - 1; i++) {
            Point<D> current = this.coordinate.get(i);
            Point<D> next = this.coordinate.get(i + 1);
            this.segments.add(new Segment<D>(current, next));
        }
    }

    public Map<Segment<D>, List<E>> aggregation() {
        Map<Segment<D>, List<E>> segmentMap = new HashMap<Segment<D>, List<E>>();
        for (Segment<D> segment : this.segments) {
            segmentMap.put(segment, this.aggregation(segment));
        }
        return segmentMap;
    }

    private List<E> aggregation(Segment<D> s) {
        List<E> segments = new ArrayList<E>();
        for (E segment : this.dataList) {
            if (segment.getStart().getPoint().compareTo(s.getStart().getPoint()) > 0) {
                continue;
            }
            if (segment.getEnd().getPoint().compareTo(s.getStart().getPoint()) > 0) {
                segments.add(segment);
            }
        }
        return segments;
    }

    public List<Segment<D>> getSegments() {
        return segments;
    }
}
