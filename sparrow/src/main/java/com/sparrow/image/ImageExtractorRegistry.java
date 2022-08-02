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

package com.sparrow.image;

import com.sparrow.protocol.enums.EditorType;
import com.sparrow.utility.StringUtility;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Named;

@Named
public class ImageExtractorRegistry {
    private Map<String, ImageExtractor> container = new HashMap<>();

    public void register(ImageExtractor instance) {
        String simpleName = instance.getClass().getSimpleName();
        simpleName = simpleName.substring(0, simpleName.indexOf("ImageExtractor"));
        this.container.put(StringUtility.humpToLower(simpleName).toUpperCase(), instance);
    }

    public ImageExtractor get(EditorType name) {
        ImageExtractor imageExtractor = this.container.get(name.name());
        if (imageExtractor == null) {
            imageExtractor = this.container.get(EditorType.HTML.name());
        }
        return imageExtractor;
    }
}
