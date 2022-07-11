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

import com.sparrow.protocol.enums.PLATFORM;
import com.sparrow.protocol.enums.StatusRecord;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TypeHandlerRegistry {
    /**
     * jdbc type map jdbc.type.name-->type.handler
     */
    private Map<String, TypeHandler> jdbcTypeHandlerMap = new HashMap<>();
    /**
     * java type java.type.name-->jdbc.type.name-->handler
     */
    private Map<String, Map<String, TypeHandler>> javaTypeHandlerMap = new HashMap<>();
    /**
     * all type handler map type.name-->handler
     */
    private Map<Class, TypeHandler> allTypeHandler = new HashMap<>();

    public void register(TypeHandler typeHandler) {
        this.allTypeHandler.put(typeHandler.getClass(), typeHandler);
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

        this.register(Long.class, LongTypeHandler.class);
        this.register(long.class, LongTypeHandler.class);
        this.register(JdbcType.BIGINT, LongTypeHandler.class);

        this.register(Integer.class, IntegerTypeHandler.class);
        this.register(int.class, IntegerTypeHandler.class);
        this.register(JdbcType.INTEGER, IntegerTypeHandler.class);

        register(Byte.class, ByteTypeHandler.class);
        register(byte.class, ByteTypeHandler.class);
        register(JdbcType.TINYINT, ByteTypeHandler.class);

        register(Boolean.class, BooleanTypeHandler.class);
        register(boolean.class, BooleanTypeHandler.class);
        register(JdbcType.BOOLEAN, BooleanTypeHandler.class);
        register(JdbcType.BIT, BooleanTypeHandler.class);

        register(Short.class, ShortTypeHandler.class);
        register(short.class, ShortTypeHandler.class);
        register(JdbcType.SMALLINT, ShortTypeHandler.class);

        register(Float.class, FloatTypeHandler.class);
        register(float.class, FloatTypeHandler.class);
        register(JdbcType.FLOAT, FloatTypeHandler.class);

        register(Double.class, DoubleTypeHandler.class);
        register(double.class, DoubleTypeHandler.class);
        register(JdbcType.DOUBLE, DoubleTypeHandler.class);

        register(PLATFORM.class, PlatformTypeHandler.class);
        register(StatusRecord.class, StatusTypeHandler.class);

        register(String.class, StringTypeHandler.class);
        register(String.class, JdbcType.CHAR, StringTypeHandler.class);
        register(String.class, JdbcType.VARCHAR, StringTypeHandler.class);

        register(JdbcType.CHAR, StringTypeHandler.class);
        register(JdbcType.VARCHAR, StringTypeHandler.class);

        register(Object.class, JdbcType.ARRAY, ArrayTypeHandler.class);
        register(JdbcType.ARRAY, ArrayTypeHandler.class);

        register(BigInteger.class, BigIntegerTypeHandler.class);

        register(BigDecimal.class, BigDecimalTypeHandler.class);
        register(JdbcType.REAL, BigDecimalTypeHandler.class);
        register(JdbcType.DECIMAL, BigDecimalTypeHandler.class);
        register(JdbcType.NUMERIC, BigDecimalTypeHandler.class);

        register(byte[].class, JdbcType.BLOB, BlobTypeHandler.class);
        register(byte[].class, JdbcType.LONGVARBINARY, BlobTypeHandler.class);
        register(JdbcType.LONGVARBINARY, BlobTypeHandler.class);
        register(JdbcType.BLOB, BlobTypeHandler.class);

//        register(String.class, JdbcType.CLOB, new ClobTypeHandler());
//        register(Reader.class, new ClobReaderTypeHandler());
//        register(String.class, JdbcType.LONGVARCHAR, new ClobTypeHandler());
//        register(String.class, JdbcType.NVARCHAR, new NStringTypeHandler());
//        register(String.class, JdbcType.NCHAR, new NStringTypeHandler());
//        register(String.class, JdbcType.NCLOB, new NClobTypeHandler());
//        register(JdbcType.CLOB, new ClobTypeHandler());
//        register(JdbcType.LONGVARCHAR, new ClobTypeHandler());
//        register(JdbcType.NVARCHAR, new NStringTypeHandler());
//        register(JdbcType.NCHAR, new NStringTypeHandler());
//        register(JdbcType.NCLOB, new NClobTypeHandler());
//        register(InputStream.class, new BlobInputStreamTypeHandler());
//        register(Byte[].class, new ByteObjectArrayTypeHandler());
//        register(Byte[].class, JdbcType.BLOB, new BlobByteObjectArrayTypeHandler());
//        register(Byte[].class, JdbcType.LONGVARBINARY, new BlobByteObjectArrayTypeHandler());
//        register(byte[].class, new ByteArrayTypeHandler());
//        register(Object.class, UNKNOWN_TYPE_HANDLER);
//        register(Object.class, JdbcType.OTHER, UNKNOWN_TYPE_HANDLER);
//        register(JdbcType.OTHER, UNKNOWN_TYPE_HANDLER);
//        register(String.class, JdbcType.SQLXML, new SqlxmlTypeHandler());
//        register(Instant.class, InstantTypeHandler.class);
//        register(LocalDateTime.class, LocalDateTimeTypeHandler.class);
//        register(LocalDate.class, LocalDateTypeHandler.class);
//        register(LocalTime.class, LocalTimeTypeHandler.class);
//        register(OffsetDateTime.class, OffsetDateTimeTypeHandler.class);
//        register(OffsetTime.class, OffsetTimeTypeHandler.class);
//        register(ZonedDateTime.class, ZonedDateTimeTypeHandler.class);
//        register(Month.class, MonthTypeHandler.class);
//        register(Year.class, YearTypeHandler.class);
//        register(YearMonth.class, YearMonthTypeHandler.class);
//        register(JapaneseDate.class, JapaneseDateTypeHandler.class);

        register(Date.class, DateTimeTypeHandler.class);
        register(JdbcType.TIMESTAMP, DateTimeTypeHandler.class);

        register(Date.class, JdbcType.DATE, DateTypeHandler.class);
        register(JdbcType.DATE, DateTypeHandler.class);

        register(Date.class, JdbcType.TIME, TimeTypeHandler.class);
        register(JdbcType.TIME, TimeTypeHandler.class);

        register(java.sql.Date.class, SqlDateTypeHandler.class);
        register(java.sql.Time.class, SqlTimeTypeHandler.class);
        register(java.sql.Timestamp.class, SqlTimestampTypeHandler.class);

        register(Character.class, CharacterTypeHandler.class);
        register(char.class, CharacterTypeHandler.class);
    }

    private TypeHandler getTypeHandler(Class javaType) {
        if (javaType == null) {
            return null;
        }
        Map<String, TypeHandler> jdbcTypeHandler = this.javaTypeHandlerMap.get(javaType.getName());
        if (jdbcTypeHandler.size() > 0) {
            TypeHandler typeHandler = jdbcTypeHandler.get(null);
            if (typeHandler != null) {
                return typeHandler;
            }
            return jdbcTypeHandler.values().iterator().next();
        }
        return null;
    }

    public TypeHandler getTypeHandler(Class javaType, JdbcType jdbcType) {
        TypeHandler typeHandler = null;
        if (javaType != null && jdbcType != null) {
            typeHandler = this.javaTypeHandlerMap.get(javaType.getName()).get(jdbcType.name());
            if (typeHandler != null) {
                return typeHandler;
            }
        }
        if (javaType != null) {
            typeHandler = this.getTypeHandler(javaType);
            if (typeHandler != null) {
                return typeHandler;
            }
        }
        return this.getTypeHandler(jdbcType);
    }

    private TypeHandler getTypeHandler(JdbcType jdbcType) {
        String jdbcTypeName = jdbcType == null ? null : jdbcType.name();
        return this.jdbcTypeHandlerMap.get(jdbcTypeName);
    }

    public void register(Class javaType, JdbcType jdbcType, Class typeHandlerClass) {
        if (javaType == null) {
            return;
        }
        Map<String, TypeHandler> jdbcTypeHandlerMap = this.javaTypeHandlerMap.get(javaType.getName());
        if (jdbcTypeHandlerMap == null) {
            jdbcTypeHandlerMap = new HashMap<>();
            this.javaTypeHandlerMap.put(javaType.getName(), jdbcTypeHandlerMap);
        }
        String jdbcTypeName = jdbcType == null ? null : jdbcType.name();
        jdbcTypeHandlerMap.put(jdbcTypeName, this.allTypeHandler.get(typeHandlerClass));
    }

    public void register(JdbcType jdbcType, Class typeHandlerClass) {
        this.jdbcTypeHandlerMap.put(jdbcType.name(), this.allTypeHandler.get(typeHandlerClass));
    }

    public void register(Class javaType, Class typeHandlerClass) {
        this.register(javaType, null, typeHandlerClass);
    }
}
