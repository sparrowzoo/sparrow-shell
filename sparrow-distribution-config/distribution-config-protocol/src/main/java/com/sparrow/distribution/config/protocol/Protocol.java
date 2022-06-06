package com.sparrow.distribution.config.protocol;

public class Protocol {
    private String fileName;
    private String body;
    private int fileNameLength;

    public Protocol() {
    }

    public Protocol(String fileName, String body) {
        this.fileName = fileName;
        this.body = body;
        this.fileNameLength=fileName.length();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getFileNameLength() {
        return fileNameLength;
    }

    public void setFileNameLength(int fileNameLength) {
        this.fileNameLength = fileNameLength;
    }
}
