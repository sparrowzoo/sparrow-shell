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
package com.sparrow.core.monitor;

import com.sparrow.core.Pair;
import com.sparrow.utility.CollectionsUtility;
import com.sparrow.utility.FileUtility;
import java.util.ArrayList;
import java.util.List;

public class ElapsedSection {

    private static final String SECTION_FILE_NAME = "monitor_section_config.properties";
    private static final List<Section> SECTIONS = new ArrayList<Section>();

    static {
        List<String> sectionConfig = FileUtility.getInstance().readLines(SECTION_FILE_NAME);
        if (!CollectionsUtility.isNullOrEmpty(sectionConfig)) {
            for (String section : sectionConfig) {
                SECTIONS.add(new Section(section));
            }
        } else {
            SECTIONS.add(new Section(0, 100));
            SECTIONS.add(new Section(100, 200));
            SECTIONS.add(new Section(200, 300));
            SECTIONS.add(new Section(300, 400));
            SECTIONS.add(new Section(400, 500));
            SECTIONS.add(new Section(500, 1000));
            SECTIONS.add(new Section(1000, 2000));
            SECTIONS.add(new Section(2000, 5000));
            SECTIONS.add(new Section(5000, Long.MAX_VALUE));
        }
    }

    public static String section(Long time) {
        if (time < 0) {
            throw new IllegalArgumentException(time + "can not support argument");
        }
        for (Section section : SECTIONS) {
            if (time >= section.getStart() && time < section.getEnd()) {
                return section.section();
            }
        }
        return null;
    }

}

class Section {
    public Section(String section) {
        Pair<String, String> sectionPair = Pair.split(section, ":");
        this.start = Long.valueOf(sectionPair.getFirst());
        this.end = Long.valueOf(sectionPair.getSecond());
    }

    public Section(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public String section() {
        String end = this.end == Long.MAX_VALUE ? "MAX" : this.end + "";
        return start + "-" + end;
    }

    private long start;
    private long end;

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }
}
