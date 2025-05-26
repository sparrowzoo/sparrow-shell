package com.sparrow.vo;

public class DataVO {
    public DataVO() {
    }

    public DataVO(String name, String type, Integer[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }

    private String name;
    private String type;
    private Integer []data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer[] getData() {
        return data;
    }

    public void setData(Integer[] data) {
        this.data = data;
    }
}
