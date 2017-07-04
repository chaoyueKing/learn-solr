package com.chaoyue.common;

import java.util.List;

/**
 * 搜索引擎查询分页类
 * Created by wangyq on 2015/10/28.
 */
public class Page<T> {
    private static final Long DEFAULT_PAGE_NO = 1L;
    private static final Long DEFAULT_PAGE_SIZE = 100L;

    private Long pageNo;//页号
    private Long pageSize;//每页行数
    private Long totalRows;//总行数
    private Long totalPages;//总页数
    private Boolean firstPage;//是否首页
    private Boolean lastPage;//是否尾页
    private List<T> result;

    public Page() {
    }

    public Page(Long pageNo, Long pageSize) {
        setPageNo(pageNo);
        setPageSize(pageSize);
    }

    public Long getPageNo() {
        return pageNo;
    }

    public void setPageNo(Long pageNo) {
        if (pageNo == null || pageNo < 1) {
            pageNo = DEFAULT_PAGE_NO;
        } else if (totalPages != null && pageNo > totalPages) {
            pageNo = totalPages;
        }
        this.pageNo = pageNo;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        if (pageSize == null) {
            this.pageSize = DEFAULT_PAGE_SIZE;
        } else if (pageSize < 1L) {
            this.pageSize = 1L;
        } else {
            this.pageSize = pageSize;
        }
    }

    public Long getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(Long totalRows) {
        //总行数
        this.totalRows = totalRows;
        //总页数
        this.totalPages = totalRows / this.pageSize;
        if (totalRows % this.pageSize > 0) {
            this.totalPages++;
        }
        //首页
        if (this.pageNo <= 1) {
            firstPage = true;
        } else {
            firstPage = false;
        }
        //尾页
        if (this.pageNo >= this.totalPages) {
            lastPage = true;
        } else {
            lastPage = false;
        }
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }

    public Boolean isFirstPage() {
        return firstPage;
    }

    public void setFirstPage(Boolean firstPage) {
        this.firstPage = firstPage;
    }

    public Boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(Boolean lastPage) {
        this.lastPage = lastPage;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Page{");
        sb.append("pageNo=").append(pageNo);
        sb.append(", pageSize=").append(pageSize);
        sb.append(", totalRows=").append(totalRows);
        sb.append(", totalPages=").append(totalPages);
        sb.append(", firstPage=").append(firstPage);
        sb.append(", lastPage=").append(lastPage);
        sb.append(", result=").append(result);
        sb.append('}');
        return sb.toString();
    }
}
