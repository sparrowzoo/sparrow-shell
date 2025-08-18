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

import java.io.File;
import java.io.InputStream;

public interface ByteSource {

    byte[] getBytes();


    String toHex();

    String toBase64();

    boolean isEmpty();


    final class Util {

        private Util() {
        }

        public static ByteSource bytes(byte[] bytes) {
            return new SimpleByteSource(bytes);
        }


        public static ByteSource bytes(char[] chars) {
            return new SimpleByteSource(chars);
        }


        public static ByteSource bytes(String string) {
            return new SimpleByteSource(string);
        }


        public static ByteSource bytes(ByteSource source) {
            return new SimpleByteSource(source);
        }


        public static ByteSource bytes(File file) {
            return new SimpleByteSource(file);
        }


        public static ByteSource bytes(InputStream stream) {
            return new SimpleByteSource(stream);
        }

        /**
         * Returns {@code true} if the specified object can be easily represented as a {@code ByteSource} using
         * the {@link Util}'s default heuristics, {@code false} otherwise.
         * <p/>
         * This implementation merely returns {@link SimpleByteSource}
         * .{@link SimpleByteSource#isCompatible(Object) isCompatible(source)}.
         *
         * @param source the object to test to see if it can be easily converted to ByteSource instances using default
         *               heuristics.
         * @return {@code true} if the specified object can be easily represented as a {@code ByteSource} using
         * the {@link Util}'s default heuristics, {@code false} otherwise.
         */
        public static boolean isCompatible(Object source) {
            return SimpleByteSource.isCompatible(source);
        }

        /**
         * Returns a {@code ByteSource} instance representing the specified byte source argument.  If the argument
         * <em>cannot</em> be easily converted to bytes (as is indicated by the {@link #isCompatible(Object)} JavaDoc),
         * this method will throw an {@link IllegalArgumentException}.
         *
         * @param source the byte-backed instance that should be represented as a {@code ByteSource} instance.
         * @return a {@code ByteSource} instance representing the specified byte source argument.
         * @throws IllegalArgumentException if the argument <em>cannot</em> be easily converted to bytes
         *                                  (as indicated by the {@link #isCompatible(Object)} JavaDoc)
         */
        public static ByteSource bytes(Object source) throws IllegalArgumentException {
            if (source == null) {
                return null;
            }
            if (!isCompatible(source)) {
                String msg = "Unable to heuristically acquire bytes for object of type ["
                        + source.getClass().getName() + "].  If this type is indeed a byte-backed data type, you might "
                        + "want to write your own ByteSource implementation to extract its bytes explicitly.";
                throw new IllegalArgumentException(msg);
            }
            if (source instanceof byte[]) {
                return bytes((byte[]) source);
            } else if (source instanceof ByteSource) {
                return (ByteSource) source;
            } else if (source instanceof char[]) {
                return bytes((char[]) source);
            } else if (source instanceof String) {
                return bytes((String) source);
            } else if (source instanceof File) {
                return bytes((File) source);
            } else if (source instanceof InputStream) {
                return bytes((InputStream) source);
            } else {
                throw new IllegalStateException("Encountered unexpected byte source.  This is a bug - please notify "
                        + "the Shiro developer list asap (the isCompatible implementation does not reflect this "
                        + "method's implementation).");
            }
        }
    }
}
