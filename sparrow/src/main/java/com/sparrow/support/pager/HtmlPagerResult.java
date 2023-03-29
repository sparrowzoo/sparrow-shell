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

package com.sparrow.support.pager;

import com.sparrow.constant.Pager;
import com.sparrow.protocol.constant.magic.Escaped;
import com.sparrow.protocol.pager.PagerResult;
import com.sparrow.utility.StringUtility;

public class HtmlPagerResult<T> extends PagerResult<T> {
    private String indexPageFormat = Pager.ACTION_PAGE_FORMAT;
    private String pageFormat = Pager.ACTION_PAGE_FORMAT;
    private Integer pageNumberCount = 5;
    private boolean simple;
    private Integer pageCount;
    private String html;

    public HtmlPagerResult(PagerResult<T> pagerResult) {
        this(pagerResult.getPageSize(), pagerResult.getPageNo(), pagerResult.getRecordTotal());
        this.list = pagerResult.getList();
    }

    private HtmlPagerResult(Integer pageSize, Integer pageNo, Long recordCount) {
        super(pageSize, pageNo);
        super.setRecordTotal(recordCount);
        this.pageCount = (int) Math.ceil(this.recordTotal / (double) this.pageSize);
    }

    public static HtmlPagerResult page(Integer pageSize, Integer pageNo, Long recordCount) {
        return new HtmlPagerResult(pageSize, pageNo, recordCount);
    }

    public String getIndexPageFormat() {
        return indexPageFormat;
    }

    public void setIndexPageFormat(String indexPageFormat) {
        this.indexPageFormat = indexPageFormat;
    }

    public String getPageFormat() {
        return pageFormat;
    }

    public void setPageFormat(String pageFormat) {
        this.pageFormat = pageFormat;
    }

    public Integer getPageNumberCount() {
        return pageNumberCount;
    }

    public void setPageNumberCount(Integer pageNumberCount) {
        this.pageNumberCount = pageNumberCount;
    }

    public boolean isSimple() {
        return simple;
    }

    public void setSimple(boolean simple) {
        this.simple = simple;
    }

    public String getHtml() {
        if (StringUtility.isNullOrEmpty(html)) {
            html = html();
        }
        return html;
    }

    /**
     * 获取分页字符串
     */
    private String html() {
        String pageNumberStyle = " class=\"num\"";
        String pageFirstStyle = "class=\"first\"";
        String pageFirstDisableStyle = "class=\"disable first\"";
        String disablePageNumStyle = "class=\"disable\"";
        StringBuilder pageString = new StringBuilder();
        pageString.append("<div id=\"divPage\" class=\"page\">\n");

        if (this.pageNo != 1) {
            if (StringUtility.isNullOrEmpty(this.indexPageFormat)) {
                pageString.append("<a " + pageFirstStyle + " href=\""
                    + this.pageFormat.replace(Pager.PAGE_INDEX, "1")
                    + "\">首页</a>\n");
            } else {
                pageString.append("<a " + pageFirstStyle + " href=\""
                    + this.indexPageFormat.replace(Pager.PAGE_INDEX, "1")
                    + "\">首页</a>\n");
            }
            if (!StringUtility.isNullOrEmpty(this.indexPageFormat)
                && this.pageNo - 1 == 1) {
                pageString.append("<a "
                    + pageNumberStyle
                    + " href=\""
                    + this.indexPageFormat.replace(Pager.PAGE_INDEX,
                    String.valueOf(this.pageNo - 1)));
                pageString.append("\">上一页");
                pageString.append("</a>\n");
            } else {
                pageString.append("<a "
                    + pageNumberStyle
                    + " href=\""
                    + this.pageFormat.replace(Pager.PAGE_INDEX,
                    String.valueOf(this.pageNo - 1)));
                pageString.append("\">上一页");
                pageString.append("</a>\n");
            }
        } else {
            pageString.append("<a ").append(pageFirstDisableStyle).append(">首页</a>\n<a ").append(disablePageNumStyle).append(">上一页</a>\n");
        }

        int beginPageIndex = (this.pageNo - 1) / pageNumberCount * pageNumberCount + 1;
        //当前只显示5个页码
        int endPageIndex = beginPageIndex + pageNumberCount - 1;
        for (Integer i = beginPageIndex; i <= endPageIndex; i++) {
            if (i > pageCount) {
                break;
            }
            if (i.equals(this.pageNo)) {
                pageString.append("<a style='color:red;font-weight:bold;'>");
                pageString.append(i);
                pageString.append("</a>\n");
            } else if (i == 1 && !StringUtility.isNullOrEmpty(this.indexPageFormat)) {
                pageString.append("<a ").append(pageNumberStyle).append(" href=\"").append(this.indexPageFormat.replace(Pager.PAGE_INDEX, "1")).append("\">").append(i).append("</a>\n");
            } else {
                pageString.append(" <a "
                    + pageNumberStyle
                    + " href=\""
                    + this.pageFormat.replace(Pager.PAGE_INDEX,
                    String.valueOf(i)) + "\">");
                pageString.append(i);
                pageString.append("</a>\n");
            }
        }

        if (this.pageNo < pageCount) {
            pageString.append("<a ").append(pageNumberStyle).append(" href=\"").append(this.pageFormat.replace(Pager.PAGE_INDEX,
                String.valueOf(this.pageNo + 1)));
            pageString.append("\">下一页");
            pageString.append("</a>\n");
            pageString.append("<a ").append(pageNumberStyle).append(" href=\"").append(this.pageFormat.replace(Pager.PAGE_INDEX,
                String.valueOf(pageCount))).append("\">末页</a>\n");
        } else {
            pageString.append("<a ").append(disablePageNumStyle).append(">下一页</a>\n<a ").append(disablePageNumStyle).append(">末页</a>\n");
        }
        if (!this.simple) {
            pageString.append("<input id=\"defPageIndex\" onmouseover=\"this.select();\" onkeyup=\"this.value=this.value.replace(/\\D/g,'');\"  onafterpaste=\"this.value=this.value.replace(/\\D/g,'');\"" + "onblur=\"if(this.value.trim()==''){this.value=parseInt($('pageNo').value)+1;}\" value=\"").append(this.pageNo + 1).append("\" type=\"text\" />\n");
            pageString.append("<a id=\"go\" onclick=\"$.page.toTargetPage(");
            pageString.append(pageCount);
            pageString.append(",'");
            pageString.append(this.pageFormat);
            pageString.append("',this);\" style='font-weight:bold'>GO</a>\n");
            pageString.append("<span style='color:#5f5f60'>\n");
            pageString.append("<a style='display:none;' id='spanpageNo'>\n");
            pageString.append(this.pageNo);
            pageString.append("</a></span>共<span>\n");
            pageString.append(pageCount);
            pageString.append("</span>页");
            pageString.append(Escaped.EM_SPACE);
            pageString.append("每页<span id='rowCountPerPage'>");
            pageString.append(this.pageSize);
            pageString.append("</span>条");
            pageString.append(Escaped.EM_SPACE);
            pageString.append("共<span id='recordTotal'>");
            pageString.append(this.recordTotal);
            pageString.append("</span>条");
            pageString.append("</a>");
        }
        pageString.append("</div>");
        return pageString.toString();
    }
}
