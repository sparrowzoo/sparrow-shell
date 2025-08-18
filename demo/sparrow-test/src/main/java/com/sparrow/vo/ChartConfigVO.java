package com.sparrow.vo;

public class ChartConfigVO {
    private String height = "500px";
    private String title;
    private String[] legend;
    private DataVO[] data;
    private String x[];

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getLegend() {
        return legend;
    }

    public void setLegend(String[] legend) {
        this.legend = legend;
    }

    public DataVO[] getData() {
        return data;
    }

    public void setData(DataVO[] data) {
        this.data = data;
    }

    public String[] getX() {
        return x;
    }

    public void setX(String[] x) {
        this.x = x;
    }
}
