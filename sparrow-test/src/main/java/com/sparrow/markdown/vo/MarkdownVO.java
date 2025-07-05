package com.sparrow.markdown.vo;

import com.sparrow.protocol.DTO;

public class MarkdownVO implements DTO {
    private String html;

    public MarkdownVO(String html) {
        this.html = html;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
