package com.sparrow.markdown.vo;

import com.sparrow.protocol.VO;

public class MarkdownVO implements VO {
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
