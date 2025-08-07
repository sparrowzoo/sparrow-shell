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
package com.sparrow.orm.type;

import com.sparrow.protocol.enums.Platform;
import com.sparrow.protocol.enums.StatusRecord;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Array;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 由于
 * 1. jpa规范没有对jdbc type 进行指定
 * 2. java 类型与jdbc type 的自动映射可以覆盖绝大多数场景
 * 故这里与mybatis 实际不完全一致，直接通过java type类型映射，简化上层调用的思考
 */
public class TypeHandlerRegistry {
    private Map<Class<?>, Class<?>> javaTypeHandlerTypeMap = new HashMap<>();

    private Map<Class<?>, TypeHandler<?>> allTypeHandlerMap = new HashMap<>();

    public void register(TypeHandler<?> typeHandler) {
        this.allTypeHandlerMap.put(typeHandler.getClass(), typeHandler);
    }

    private static class Nested {
        static TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();
    }

    public static TypeHandlerRegistry getInstance() {
        return Nested.typeHandlerRegistry;
    }

    private TypeHandlerRegistry() {
        this.register(new LongTypeHandler());
        this.register(new IntegerTypeHandler());
        this.register(new ByteTypeHandler());
        this.register(new FloatTypeHandler());
        this.register(new DoubleTypeHandler());
        this.register(new StringTypeHandler());
        this.register(new DateTypeHandler());
        this.register(new TimeTypeHandler());
        this.register(new DateTimeTypeHandler());
        this.register(new SqlDateTypeHandler());
        this.register(new SqlTimestampTypeHandler());
        this.register(new ShortTypeHandler());
        this.register(new BigIntegerTypeHandler());
        this.register(new BigDecimalTypeHandler());
        this.register(new ArrayTypeHandler());
        this.register(new BlobTypeHandler());
        this.register(new BooleanTypeHandler());
        this.register(new CharacterTypeHandler());
        this.register(new StatusTypeHandler());
        this.register(new PlatformTypeHandler());
        this.register(new LocalDateTypeHandler());

        this.register(Long.class, LongTypeHandler.class);
        this.register(long.class, LongTypeHandler.class);

        this.register(Integer.class, IntegerTypeHandler.class);
        this.register(int.class, IntegerTypeHandler.class);

        register(Byte.class, ByteTypeHandler.class);
        register(byte.class, ByteTypeHandler.class);

        register(Boolean.class, BooleanTypeHandler.class);
        register(boolean.class, BooleanTypeHandler.class);

        register(Short.class, ShortTypeHandler.class);
        register(short.class, ShortTypeHandler.class);

        register(Float.class, FloatTypeHandler.class);
        register(float.class, FloatTypeHandler.class);

        register(Double.class, DoubleTypeHandler.class);
        register(double.class, DoubleTypeHandler.class);

        register(Platform.class, PlatformTypeHandler.class);
        register(StatusRecord.class, StatusTypeHandler.class);

        register(String.class, StringTypeHandler.class);
        register(Array.class, ArrayTypeHandler.class);
        register(BigInteger.class, BigIntegerTypeHandler.class);
        register(BigDecimal.class, BigDecimalTypeHandler.class);

        register(byte[].class, BlobTypeHandler.class);
        register(Date.class, DateTimeTypeHandler.class);


        register(java.sql.Date.class, SqlDateTypeHandler.class);
        register(java.sql.Time.class, SqlTimeTypeHandler.class);
        register(java.sql.Timestamp.class, SqlTimestampTypeHandler.class);
        register(LocalDate.class, LocalDateTypeHandler.class);
        register(LocalTime.class, LocalTimeTypeHandler.class);


        register(Character.class, CharacterTypeHandler.class);
        register(char.class, CharacterTypeHandler.class);

    }


    public TypeHandler<?> getTypeHandler(Class<?> javaType) {
        if (javaType == null) {
            return null;
        }
        Class<?> typeHandlerClazz = this.javaTypeHandlerTypeMap.get(javaType);
        if (typeHandlerClazz == null) {
            return null;
        }
        return this.allTypeHandlerMap.get(typeHandlerClazz);
    }


    public void register(Class<?> javaType, Class<?> typeHandlerClass) {
        if (javaType == null) {
            return;
        }
        this.javaTypeHandlerTypeMap.put(javaType, typeHandlerClass);
    }
}
