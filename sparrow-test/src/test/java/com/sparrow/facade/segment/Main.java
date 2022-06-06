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

package com.sparrow.facade.segment;

import com.sparrow.constant.DateTime;
import com.sparrow.constant.DateTime;
import com.sparrow.core.algorithm.gouping.Coordinate;
import com.sparrow.core.algorithm.gouping.Point;
import com.sparrow.core.algorithm.gouping.Segment;
import com.sparrow.enums.DateTimeUnit;
import com.sparrow.utility.DateTimeUtility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author harry
 */
public class Main {
    static class IntegerCoordinate extends Coordinate<BusinessSegment, Long> {

        public IntegerCoordinate(List<BusinessSegment> dataList) {
            super(dataList);
        }

        @Override
        public void section() {
            for (Point<Long> point : this.coordinate) {
                System.out.println(DateTimeUtility.getFormatTime(point.getPoint(), DateTime.FORMAT_YYYY_MM_DD_HH_MM_SS));
            }
            for (int i = 0; i < this.coordinate.size() - 1; i++) {

                Point<Long> current = this.coordinate.get(i);
                Point<Long> start = Point.copy(current);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(start.getPoint());
                if (calendar.get(Calendar.HOUR_OF_DAY) == 23) {
                    calendar.add(Calendar.SECOND, 1);
                    start.setPoint(calendar.getTimeInMillis());
                }

                Point<Long> next = this.coordinate.get(i + 1);
                Point<Long> end = Point.copy(next);
                if (DateTimeUtility.getInterval(start.getPoint(), next.getPoint(), DateTimeUnit.SECOND) <= 5) {
                    i++;
                    continue;
                }

                calendar.setTimeInMillis(end.getPoint());
                if (calendar.get(Calendar.HOUR_OF_DAY) == 0) {
                    calendar.add(Calendar.SECOND, -1);
                    end.setPoint(calendar.getTimeInMillis());
                }
                System.out.println(DateTimeUtility.getFormatTime(start.getPoint(), DateTime.FORMAT_YYYY_MM_DD_HH_MM_SS) + "-" +
                        DateTimeUtility.getFormatTime(end.getPoint(), DateTime.FORMAT_YYYY_MM_DD_HH_MM_SS));
                this.segments.add(new Segment<Long>(start, end));
            }
        }
    }

    enum SEGMENT_TYPE {
        TYPE_001,
        TYPE_002,

        TYPE_003,
        TYPE_004,

        TYPE_005,
        TYPE_006,

        TYPE_007,
        TYPE_008,

        TYPE_009,
        TYPE_010
    }

    public static void main(String[] args) {
        List<BusinessSegment> list = new ArrayList<BusinessSegment>();

        //Long id, String type, Integer start, Integer end
        list.add(new BusinessSegment(1L, SEGMENT_TYPE.TYPE_001.name(), DateTimeUtility.parse("2018-02-05 00:00:00", DateTime.FORMAT_YYYY_MM_DD_HH_MM_SS), DateTimeUtility.parse("2018-02-11 23:59:59", DateTime.FORMAT_YYYY_MM_DD_HH_MM_SS)));
        list.add(new BusinessSegment(1L, SEGMENT_TYPE.TYPE_002.name(), DateTimeUtility.parse("2018-02-06 00:00:00", DateTime.FORMAT_YYYY_MM_DD_HH_MM_SS), DateTimeUtility.parse("2018-02-10 23:59:59", DateTime.FORMAT_YYYY_MM_DD_HH_MM_SS)));
        list.add(new BusinessSegment(1L, SEGMENT_TYPE.TYPE_004.name(), DateTimeUtility.parse("2018-02-07 00:00:00", DateTime.FORMAT_YYYY_MM_DD_HH_MM_SS), DateTimeUtility.parse("2018-02-09 23:59:59", DateTime.FORMAT_YYYY_MM_DD_HH_MM_SS)));
        list.add(new BusinessSegment(1L, SEGMENT_TYPE.TYPE_005.name(), DateTimeUtility.parse("2018-02-08 00:00:00", DateTime.FORMAT_YYYY_MM_DD_HH_MM_SS), DateTimeUtility.parse("2018-02-08 23:59:59", DateTime.FORMAT_YYYY_MM_DD_HH_MM_SS)));
        list.add(new BusinessSegment(1L, SEGMENT_TYPE.TYPE_006.name(), DateTimeUtility.parse("2018-02-05 00:00:00", DateTime.FORMAT_YYYY_MM_DD_HH_MM_SS), DateTimeUtility.parse("2018-02-19 23:59:59", DateTime.FORMAT_YYYY_MM_DD_HH_MM_SS)));
        list.add(new BusinessSegment(1L, SEGMENT_TYPE.TYPE_007.name(), DateTimeUtility.parse("2018-02-20 00:00:00", DateTime.FORMAT_YYYY_MM_DD_HH_MM_SS), DateTimeUtility.parse("2018-02-22 23:59:59", DateTime.FORMAT_YYYY_MM_DD_HH_MM_SS)));
        list.add(new BusinessSegment(1L, SEGMENT_TYPE.TYPE_008.name(), DateTimeUtility.parse("2018-02-21 00:00:00", DateTime.FORMAT_YYYY_MM_DD_HH_MM_SS), DateTimeUtility.parse("2018-02-23 23:59:59", DateTime.FORMAT_YYYY_MM_DD_HH_MM_SS)));
        list.add(new BusinessSegment(1L, SEGMENT_TYPE.TYPE_009.name(), DateTimeUtility.parse("2018-02-22 00:00:00", DateTime.FORMAT_YYYY_MM_DD_HH_MM_SS), DateTimeUtility.parse("2018-02-24 23:59:59", DateTime.FORMAT_YYYY_MM_DD_HH_MM_SS)));
        list.add(new BusinessSegment(1L, SEGMENT_TYPE.TYPE_010.name(), DateTimeUtility.parse("2018-02-23 00:00:00", DateTime.FORMAT_YYYY_MM_DD_HH_MM_SS), DateTimeUtility.parse("2018-02-25 23:59:59", DateTime.FORMAT_YYYY_MM_DD_HH_MM_SS)));
        list.add(new BusinessSegment(1L, SEGMENT_TYPE.TYPE_010.name(), DateTimeUtility.parse("2018-02-25 00:00:00", DateTime.FORMAT_YYYY_MM_DD_HH_MM_SS), DateTimeUtility.parse("2018-02-28 23:59:59", DateTime.FORMAT_YYYY_MM_DD_HH_MM_SS)));


        Coordinate<BusinessSegment, Long> coordinate = new IntegerCoordinate(list);
        coordinate.draw();
        coordinate.section();

        Map<Segment<Long>, List<BusinessSegment>> map = coordinate.aggregation();
        for (Segment segment : coordinate.getSegments()) {
            System.out.print(DateTimeUtility.getFormatTime((Long) segment.getStart().getPoint(), DateTime.FORMAT_YYYY_MM_DD) + "-");
            System.out.println(DateTimeUtility.getFormatTime((Long) segment.getEnd().getPoint(), DateTime.FORMAT_YYYY_MM_DD));
            System.out.println(map.get(segment));
        }
    }
}
