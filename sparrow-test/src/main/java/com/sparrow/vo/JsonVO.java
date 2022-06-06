package com.sparrow.vo;

import com.sparrow.protocol.VO;

public class JsonVO implements VO {
    public JsonVO(String json) {
        this.json = json;
    }

    private String json;

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
