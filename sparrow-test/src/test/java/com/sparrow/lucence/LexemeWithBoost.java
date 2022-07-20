/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sparrow.lucence;

/**
 * IK词元对象
 */
public class LexemeWithBoost implements Comparable<LexemeWithBoost> {
    public static final Integer LIMIT_SCORE = 50;
    public static final Integer SINGLEWORD_SCORE = 0;
    public static final Integer NORMALWORD_SCORE = 80;
    public static final Integer WHOLEWORD_SCORE = 100;
    //lexemeType常量
    //未知
    public static final int TYPE_UNKNOWN = 0;
    //英文
    public static final int TYPE_ENGLISH = 1;
    //数字
    public static final int TYPE_ARABIC = 2;
    //英文数字混合
    public static final int TYPE_LETTER = 3;
    //中文词元
    public static final int TYPE_CNWORD = 4;
    //中文单字
    public static final int TYPE_CNCHAR = 64;
    //日韩文字
    public static final int TYPE_OTHER_CJK = 8;
    //中文数词
    public static final int TYPE_CNUM = 16;
    //中文量词
    public static final int TYPE_COUNT = 32;
    //中文数量词
    public static final int TYPE_CQUAN = 48;

    //词元的起始位移
    private int offset;
    //词元的相对起始位置
    private int begin;
    //词元的长度
    private int length;
    //词元文本
    private String lexemeText;
    //词元类型
    private String lexemeType;
    //父词元
    private LexemeWithBoost parent;
    //权重
    private int boost;

    public LexemeWithBoost(int offset, int begin, int length, String lexemeType) {
        this.offset = offset;
        this.begin = begin;
        if (length < 0) {
            throw new IllegalArgumentException("length < 0");
        }
        this.length = length;
        this.lexemeType = lexemeType;
    }

    /*
     * 判断词元相等算法
     * 起始位置偏移、起始位置、终止位置相同
     * @see java.lang.Object#equals(Object o)
     */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (this == o) {
            return true;
        }

        if (o instanceof LexemeWithBoost) {
            LexemeWithBoost other = (LexemeWithBoost) o;
            if (this.offset == other.getOffset()
                && this.begin == other.getBegin()
                && this.length == other.getLength()) {
                return true;
            }
            return false;
        }
        return false;
    }

    /*
     * 词元哈希编码算法
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        int absBegin = getBeginPosition();
        int absEnd = getEndPosition();
        return (absBegin * 37) + (absEnd * 31) + ((absBegin * absEnd) % getLength()) * 11;
    }

    /*
     * 词元在排序集合中的比较算法
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(LexemeWithBoost other) {
        //起始位置优先
        if (this.begin < other.getBegin()) {
            return -1;
        } else if (this.begin == other.getBegin()) {
            //词元长度优先
            if (this.length > other.getLength()) {
                return -1;
            } else if (this.length == other.getLength()) {
                return 0;
            } else {
                return 1;
            }

        } else {
            return 1;
        }
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getBegin() {
        return begin;
    }

    /**
     * 获取词元在文本中的起始位置
     *
     * @return int
     */
    public int getBeginPosition() {
        return offset + begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    /**
     * 获取词元在文本中的结束位置
     *
     * @return int
     */
    public int getEndPosition() {
        return offset + begin + length;
    }

    /**
     * 获取词元的字符长度
     *
     * @return int
     */
    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        if (this.length < 0) {
            throw new IllegalArgumentException("length < 0");
        }
        this.length = length;
    }

    /**
     * 获取词元的文本内容
     *
     * @return String
     */
    public String getLexemeText() {
        if (lexemeText == null) {
            return "";
        }
        return lexemeText;
    }

    public void setLexemeText(String lexemeText) {
        if (lexemeText == null) {
            this.lexemeText = "";
            this.length = 0;
        } else {
            this.lexemeText = lexemeText;
            this.length = lexemeText.length();
        }
    }

    /**
     * 获取词元类型
     *
     * @return int
     */
    public String getLexemeType() {
        return lexemeType;
    }

    public static int getLexemeType(String type) {
        switch (type) {

            case "ENGLISH":
                return TYPE_ENGLISH;

            case "ARABIC":
                return TYPE_ARABIC;

            case "LETTER":
                return TYPE_LETTER;

            case "CN_WORD":
                return TYPE_CNWORD;

            case "CN_CHAR":
                return TYPE_CNCHAR;

            case "OTHER_CJK":
                return TYPE_OTHER_CJK;

            case "COUNT":
                return TYPE_COUNT;

            case "TYPE_CNUM":
                return TYPE_CNUM;

            case "TYPE_CQUAN":
                return TYPE_CQUAN;
            default:
                return TYPE_UNKNOWN;
        }
    }

    public LexemeWithBoost getParent() {
        return parent;
    }

    public void setParent(LexemeWithBoost parent) {
        this.parent = parent;
    }

    public int getBoost() {
        return boost;
    }

    public void setBoost(int boost) {
        this.boost = boost;
    }

    public void setLexemeType(String lexemeType) {
        this.lexemeType = lexemeType;
    }

    /**
     * 合并两个相邻的词元
     *
     * @param l
     * @param lexemeType
     * @return boolean 词元是否成功合并
     */
    public boolean append(LexemeWithBoost l, String lexemeType) {
        if (l != null && this.getEndPosition() == l.getBeginPosition()) {
            this.length += l.getLength();
            this.lexemeType = lexemeType;
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     */
    public String toHumanString() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append(this.getBeginPosition()).append("-").append(this.getEndPosition());
        strbuf.append(" : ").append(this.lexemeText).append(" : \t");
        strbuf.append(this.getLexemeType());
        return strbuf.toString();
    }

    @Override
    public String toString() {
        return "LexemeWithBoost{" +
            "offset=" + offset +
            ", begin=" + begin +
            ", length=" + length +
            ", lexemeText='" + lexemeText + '\'' +
            ", lexemeType=" + lexemeType +
            ", parent=" + (parent == null ? "" : parent.getLexemeText()) +
            ", boost=" + boost +
            '}';
    }
}
