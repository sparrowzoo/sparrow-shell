package com.sparrow.markdown.parser.impl;

import com.sparrow.markdown.mark.MARK;
import com.sparrow.markdown.mark.MARK_STATUS;
import com.sparrow.markdown.parser.MarkParser;
import com.sparrow.support.protocol.pager.PagerResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harry on 2018/2/6.
 */
public class MarkdownParserComposite implements MarkParser {

    private String content;

    /**
     * 当前字符指针
     */
    private int currentPointer;

    /**
     * 当前mark
     */
    private MARK currentMark;


    private MARK_STATUS markStatus=MARK_STATUS.START_DETECTING;

    private char[]contentCharArray=null;
    /**
     * 上一个mark
     */
    private MARK previousMark;

    private StringBuilder currentDetecting;

    private StringBuilder currentDetectingMark;

    private List<MarkParser> markParserList = new ArrayList<MarkParser>();

    public MarkdownParserComposite(String content) {
        this.content = content;
    }

    private MARK detectStartMark() {
        char current=this.contentCharArray[this.currentPointer];
        for (MARK mark : MARK.CONTAINER) {
            if (mark.getStart().startsWith(current))) {
                currentDetectingMark.delete()
                continue;
            }
            return mark;
        }
        return null;
    }

    private MARK detectEndMark() {
        for (MARK mark : MARK.CONTAINER) {
            if (!currentDetectingMark.toString().equals(mark.getEnd())) {
                continue;
            }
            return mark;
        }
        return null;
    }

    @Override
    public String parse() {
        this.contentCharArray= this.content.toCharArray();
        for (char c : this.contentCharArray) {
            this.currentPointer++;
            currentDetecting.append(c);
            if(markStatus.equals(MARK_STATUS.START_DETECTING)) {
                MARK mark = this.detectStartMark();
                if (mark != null) {
                    currentMark=mark;

                }
            }
        }
    }
}
