package com.sparrow.vo;

import com.sparrow.protocol.DTO;

public class JsonVO implements DTO {
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
