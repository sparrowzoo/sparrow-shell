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
package com.sparrow.lang;

import com.sparrow.lang.codec.CodecSupport;
import com.sparrow.lang.codec.Hex;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;

public class SimpleByteSource implements ByteSource {

    private final byte[] bytes;
    private String cachedHex;
    private String cachedBase64;

    public SimpleByteSource(byte[] bytes) {
        this.bytes = bytes;
    }

    public SimpleByteSource(char[] chars) {
        this.bytes = CodecSupport.toBytes(chars);
    }

    public SimpleByteSource(String string) {
        this.bytes = CodecSupport.toBytes(string);
    }

    public SimpleByteSource(ByteSource source) {
        this.bytes = source.getBytes();
    }


    public SimpleByteSource(File file) {
        this.bytes = new BytesHelper().getBytes(file);
    }

    public SimpleByteSource(InputStream stream) {
        this.bytes = new BytesHelper().getBytes(stream);
    }

    public static boolean isCompatible(Object o) {
        return o instanceof byte[] || o instanceof char[] || o instanceof String
                || o instanceof ByteSource || o instanceof File || o instanceof InputStream;
    }

    public static ByteSource empty() {
        return new SimpleByteSource(new byte[] {});
    }

    @Override
    public byte[] getBytes() {
        return Arrays.copyOf(this.bytes, this.bytes.length);
    }

    @Override
    public boolean isEmpty() {
        return this.bytes == null || this.bytes.length == 0;
    }

    @Override
    public String toHex() {
        if (this.cachedHex == null) {
            this.cachedHex = Hex.encodeToString(getBytes());
        }
        return this.cachedHex;
    }

    @Override
    public String toBase64() {
        if (this.cachedBase64 == null) {
            this.cachedBase64 = Base64.getEncoder().encodeToString(getBytes());
        }
        return this.cachedBase64;
    }

    @Override
    public String toString() {
        return toBase64();
    }

    @Override
    public int hashCode() {
        if (this.bytes == null || this.bytes.length == 0) {
            return 0;
        }
        return Arrays.hashCode(this.bytes);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ByteSource) {
            ByteSource bs = (ByteSource) o;
            return Arrays.equals(getBytes(), bs.getBytes());
        }
        return false;
    }


    private static final class BytesHelper extends CodecSupport {
        public byte[] getBytes(File file) {
            return toBytes(file);
        }

        public byte[] getBytes(InputStream stream) {
            return toBytes(stream);
        }
    }
}
