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

package com.sparrow.protocol;

import com.sparrow.protocol.constant.magic.DIGIT;
import com.sparrow.protocol.constant.magic.SYMBOL;

/**
 * @author harry
 */
public class Size implements POJO{
    private String width;
    private String height;

    public Size(int width, int height) {
        this.width = String.valueOf(width);
        this.height = String.valueOf(height);
    }

    public Size(String size) {
        String[] sizeArray = size.split("\\*");
        this.width = sizeArray[0];
        this.height = sizeArray[1];
    }

    public String getWidthPX() {
        return width + "px";
    }

    public String getHeightPX() {
        if (SYMBOL.STAR.equals(height)) {
            return "auto";
        }
        return height + "px";
    }

    public int getWidth() {
        return Integer.valueOf(width);
    }

    public int getHeight() {
        return SYMBOL.STAR.equals(height) ? DIGIT.ALL : Integer.valueOf(this.height);
    }

    @Override
    public String toString() {
        String size = "width:" + this.width + "px";
        if (!SYMBOL.STAR.equals(height)) {
            size += "-height:" + this.height + "px";
        } else {
            size += "-height:auto";
        }
        return size;
    }

    public String getContainerWidth() {
        if (Integer.valueOf(this.width) > 500) {
            return "500";
        }
        return this.width;
    }

    public String getContainerWidthPX() {
        return this.getContainerWidth() + "px";
    }

    public String getContainerHeightPX() {
        if (SYMBOL.STAR.equals(this.height)) {
            return "auto";
        }
        double scale = Double.valueOf(this.getContainerWidth()) / Integer.valueOf(this.width);
        Double height = Math.ceil(Integer.valueOf(this.height) * scale);
        return height.intValue() + "px";
    }
}
