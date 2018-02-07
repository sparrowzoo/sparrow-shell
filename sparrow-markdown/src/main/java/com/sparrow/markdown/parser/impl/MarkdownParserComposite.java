package com.sparrow.markdown.parser.impl;

import com.sparrow.markdown.mark.MARK;
import com.sparrow.markdown.mark.MARK_STATUS;
import com.sparrow.markdown.parser.MarkParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by harry on 2018/2/6.
 */
public class MarkdownParserComposite implements MarkParser {

    private static final int MAX_MARK_LENGTH=7;

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

    private String readMaxMark(){
          return new String(Arrays.copyOfRange(this.contentCharArray,this.currentPointer,this.currentPointer+MAX_MARK_LENGTH));
    }

    private String readLine(){

    }
    public MarkdownParserComposite(String content) {
        this.content = content;
    }

    private MARK detectStartMark() {
        if(this.currentDetectingMark.length()==0){
            if(this.currentPointer!=0||(this.currentPointer>2)){
                if(this.currentPointer)
            }

            ||(this.currentPointer>2&&this.contentCharArray[])
        }
        if(this.currentPointer==0||)
        currentDetectingMark.append(this.contentCharArray[this.currentPointer]);
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
