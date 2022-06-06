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
