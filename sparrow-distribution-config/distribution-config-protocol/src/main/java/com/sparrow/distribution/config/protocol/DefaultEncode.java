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
package com.sparrow.distribution.config.protocol;

import java.nio.ByteBuffer;

public class DefaultEncode implements ProtocolEndec {
    @Override
    public byte[] encode(Protocol protocol) {
        byte[] fileNameBytes = protocol.getFileName().getBytes();
        byte[] bodyBytes = protocol.getBody().getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(fileNameBytes.length + bodyBytes.length + Integer.SIZE / 8);
        buffer.putInt(fileNameBytes.length);
        buffer.put(fileNameBytes);
        buffer.put(bodyBytes);
        buffer.flip();
        byte[] newArray = new byte[buffer.remaining()];
        buffer.get(newArray);
        return newArray;
    }

    @Override
    public Protocol decode(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        Protocol protocol = new Protocol();
        protocol.setFileNameLength(buffer.getInt());
        byte[] fileNameBytes = new byte[protocol.getFileNameLength()];
        buffer.get(fileNameBytes, buffer.arrayOffset(), protocol.getFileNameLength());
        protocol.setFileName(new String(fileNameBytes));
        int fileBodyLength = bytes.length - protocol.getFileNameLength();
        byte[] bodyBytes = new byte[fileBodyLength];
        buffer.get(bodyBytes, buffer.arrayOffset(), fileBodyLength);
        protocol.setBody(new String(bodyBytes));
        return protocol;
    }
}
