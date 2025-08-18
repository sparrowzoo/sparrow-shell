/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.sparrow.lang.codec;


import com.sparrow.lang.ByteSource;

import java.io.*;
import java.nio.charset.StandardCharsets;

public abstract class CodecSupport {

    public static byte[] toBytes(char[] chars) throws CodecException {
        return toBytes(new String(chars), StandardCharsets.UTF_8.name());
    }


    public static byte[] toBytes(char[] chars, String encoding) throws CodecException {
        return toBytes(new String(chars), encoding);
    }


    public static byte[] toBytes(String source) {
        return toBytes(source, StandardCharsets.UTF_8.name());
    }


    public static byte[] toBytes(String source, String encoding) throws CodecException {
        try {
            return source.getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            String msg = "Unable to convert source [" + source + "] to byte array using "
                    + "encoding '" + encoding + "'";
            throw new CodecException(msg, e);
        }
    }


    public static String toString(byte[] bytes) {
        return toString(bytes, StandardCharsets.UTF_8.name());
    }


    public static String toString(byte[] bytes, String encoding) throws CodecException {
        try {
            return new String(bytes, encoding);
        } catch (UnsupportedEncodingException e) {
            String msg = "Unable to convert byte array to String with encoding '" + encoding + "'.";
            throw new CodecException(msg, e);
        }
    }


    public static char[] toChars(byte[] bytes) {
        return toChars(bytes, StandardCharsets.UTF_8.name());
    }


    public static char[] toChars(byte[] bytes, String encoding) throws CodecException {
        return toString(bytes, encoding).toCharArray();
    }


    protected boolean isByteSource(Object o) {
        return o instanceof byte[] || o instanceof char[] || o instanceof String
                || o instanceof ByteSource || o instanceof File || o instanceof InputStream;
    }

    protected byte[] toBytes(Object object) {
        if (object == null) {
            String msg = "Argument for byte conversion cannot be null.";
            throw new IllegalArgumentException(msg);
        }
        if (object instanceof byte[]) {
            return (byte[]) object;
        } else if (object instanceof ByteSource) {
            return ((ByteSource) object).getBytes();
        } else if (object instanceof char[]) {
            return toBytes((char[]) object);
        } else if (object instanceof String) {
            return toBytes((String) object);
        } else if (object instanceof File) {
            return toBytes((File) object);
        } else if (object instanceof InputStream) {
            return toBytes((InputStream) object);
        } else {
            return objectToBytes(object);
        }
    }


    protected String toString(Object o) {
        if (o == null) {
            String msg = "Argument for String conversion cannot be null.";
            throw new IllegalArgumentException(msg);
        }
        if (o instanceof byte[]) {
            return toString((byte[]) o);
        } else if (o instanceof char[]) {
            return new String((char[]) o);
        } else if (o instanceof String) {
            return (String) o;
        } else {
            return objectToString(o);
        }
    }

    protected byte[] toBytes(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File argument cannot be null.");
        }
        try {
            return toBytes(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            String msg = "Unable to acquire InputStream for file [" + file + "]";
            throw new CodecException(msg, e);
        }
    }


    protected byte[] toBytes(InputStream in) {
        if (in == null) {
            throw new IllegalArgumentException("InputStream argument cannot be null.");
        }
        final int bufferSize = 512;
        ByteArrayOutputStream out = new ByteArrayOutputStream(bufferSize);
        byte[] buffer = new byte[bufferSize];
        int bytesRead;
        try {
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            return out.toByteArray();
        } catch (IOException ioe) {
            throw new CodecException(ioe);
        } finally {
            try {
                in.close();
            } catch (IOException ignored) {
            }
            try {
                out.close();
            } catch (IOException ignored) {
            }
        }
    }


    protected byte[] objectToBytes(Object o) {
        String msg = "The " + getClass().getName() + " implementation only supports conversion to "
                + "byte[] if the source is of type byte[], char[], String, " + ByteSource.class.getName()
                + " File or InputStream.  The instance provided as a method "
                + "argument is of type [" + o.getClass().getName() + "].  If you would like to convert "
                + "this argument type to a byte[], you can 1) convert the argument to one of the supported types "
                + "yourself and then use that as the method argument or 2) subclass " + getClass().getName()
                + "and override the objectToBytes(Object o) method.";
        throw new CodecException(msg);
    }


    protected String objectToString(Object o) {
        return o.toString();
    }
}
