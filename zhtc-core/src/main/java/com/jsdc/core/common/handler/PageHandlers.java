package com.jsdc.core.common.handler;

import com.jsdc.core.base.Base;

import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

public class PageHandlers extends Base implements java.io.Serializable {

   private Integer pageIndex = 0;

   private Integer pageSize = 20;

    public PageHandlers(Integer pageIndex, Integer pageSize) {
        this.pageIndex = notEmpty(pageIndex) ? pageIndex : this.pageIndex;
        this.pageSize = notEmpty(pageSize) ? pageSize : this.pageSize;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
