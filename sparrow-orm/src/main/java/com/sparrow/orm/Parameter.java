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

package com.sparrow.orm;

/**
 * SQLParameter SQL参数
 * <p/>
 * 因为数据可能为null 获取不到原来的类型
 * <p/>
 * 故需此类保存实际类型信息
 * <p/>
 * 只有ORM反射使用包外不允许使用
 *
 * @version 1.0
 */
public class Parameter {
    private String name;
    private Object value;
    private Class type;
    private int scale;

    public Object getParameterValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Parameter(Object parameterValue) {
        this.value = parameterValue;
    }

    public Parameter(Field field,
        Object parameterValue) {
        this.name = field.getName();
        this.type = field.getType();
        this.value = parameterValue;
        this.scale = field.getScale();
    }

    public int getScale() {
        return scale;
    }

    public Class getType() {
        return type;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }
}
