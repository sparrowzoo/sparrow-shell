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

package com.sparrow.core;

import com.sparrow.core.spi.JsonFactory;
import com.sparrow.json.Json;
import com.sparrow.protocol.POJO;
import com.sparrow.protocol.enums.StatusRecord;
import com.sparrow.utility.StringUtility;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@SuppressWarnings("unchecked")
public class TypeConverter {
    public TypeConverter() {
    }

    /**
     * 字段名
     */
    protected String name;
    /**
     * 当前值
     */
    protected Object value;
    /**
     * 目标类型
     */
    protected Class type;

    private Json json = JsonFactory.getProvider();

    /**
     * 实体对象
     *
     * @param name
     * @param value
     * @param type
     */
    public TypeConverter(String name, Object value, Class type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public TypeConverter(String name, Class type) {
        this(name, null, type);
    }

    public TypeConverter(Class type) {
        this(null, null, type);
    }

    public String getName() {
        return name;
    }

    public Class getType() {
        return type;
    }

    public Object convert(Object value) {
        this.value = value;
        return this.convert();
    }

    public static Integer getInteger(Object value) {
        return (Integer) new TypeConverter(Integer.class).convert(value);
    }

    public static String getString(Object value) {
        return (String) new TypeConverter(String.class).convert(value);
    }

    public static Boolean getBoolean(Object value) {
        return (Boolean) new TypeConverter(Boolean.class).convert(value);
    }

    public static Long getLong(Object value) {
        return (Long) new TypeConverter(Long.class).convert(value);
    }

    public static BigDecimal getDecimal(Object value) {
        return (BigDecimal) new TypeConverter(BigDecimal.class).convert(value);
    }

    public static Date getDate(Object value) {
        return (Date) new TypeConverter(Date.class).convert(value);
    }

    public static Timestamp getTimestamp(Object value) {
        return (Timestamp) new TypeConverter(Timestamp.class).convert(value);
    }

    public Object convert() {
        if (StringUtility.isNullOrEmpty(value)) {
            return null;
        }

        if (this.getType() == Object.class) {
            return value;
        }
        try {
            Class valueType = value.getClass();
            if (this.getType() == String.class) {
                //当前值是entity对象则转json
                if (POJO.class.isAssignableFrom(valueType)) {
                    return json.toString((POJO) value);
                }
                return value.toString();
            }
            String stringValue = value.toString();
            if (this.getType() == byte.class || this.getType() == Byte.class) {
                return Byte.valueOf(stringValue);
            }
            if (this.getType() == int.class || this.getType() == Integer.class) {
                return Integer.valueOf(stringValue);
            }
            if (this.getType() == long.class || this.getType() == Long.class) {
                return Long.valueOf(stringValue);
            }
            if (this.getType() == Date.class) {
                return Date.valueOf(stringValue);
            }
            if (this.getType() == Timestamp.class) {
                if (value.toString().length() <= 10) {
                    return Timestamp.valueOf(stringValue + " 00:00:00");
                }
                return Timestamp.valueOf(stringValue);
            }
            if (this.getType() == boolean.class || this.getType() == Boolean.class) {
                boolean b = false;
                if (!StringUtility.isNullOrEmpty(stringValue)) {
                    if (String.valueOf(StatusRecord.ENABLE
                        .ordinal()).equals(stringValue) || Boolean.TRUE.toString().equalsIgnoreCase(stringValue)) {
                        b = true;
                    }
                }
                return b;
            }
            if (this.getType() == double.class || this.getType() == Double.class) {
                return Double.valueOf(stringValue);
            }
            if (this.getType() == BigDecimal.class) {
                //留给业务处理
                return new BigDecimal(stringValue);
            }
            //转成实例对象
            if (POJO.class.isAssignableFrom(this.getType())) {
                return json.parse(stringValue, this.getType());
            }
        } catch (RuntimeException e) {
            return null;
        }
        return null;
    }
}
