package com.sparrow.protocol.pager;

import com.sparrow.protocol.POJO;

/**
 * simpler pager without html
 */
public class SimplePager implements POJO {

    public SimplePager() {
    }

    /**
     * page size
     */
    protected Integer pageSize = 10;
    /**
     * current page index
     */
    protected Integer currentPageIndex;

    public SimplePager(Integer pageSize, Integer currentPageIndex) {
        if (pageSize != null) {
            this.pageSize = pageSize;
        }
        if (currentPageIndex == null) {
            currentPageIndex = 1;
        }
        this.currentPageIndex = currentPageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getCurrentPageIndex() {
        return currentPageIndex;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setCurrentPageIndex(Integer currentPageIndex) {
        this.currentPageIndex = currentPageIndex;
    }
}
