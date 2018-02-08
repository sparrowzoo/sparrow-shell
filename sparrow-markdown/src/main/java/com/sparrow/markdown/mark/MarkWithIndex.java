package com.sparrow.markdown.mark;

/**
 * Created by harry on 2018/2/8.
 */
public class MarkWithIndex {
    private MARK mark;
    private Integer pointer;

    public MarkWithIndex(MARK mark, Integer pointer) {
        this.mark = mark;
        this.pointer = pointer;
    }

    public MARK getMark() {
        return mark;
    }

    public void setMark(MARK mark) {
        this.mark = mark;
    }

    public Integer getPointer() {
        return pointer;
    }

    public void setPointer(Integer pointer) {
        this.pointer = pointer;
    }
}
