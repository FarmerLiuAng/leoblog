package com.mashenzhilu.blog.vo.params;

import lombok.Data;

@Data
public class PageParams {
    private int page = 1;

    private int pagesize = 10;

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    private Long categoryId;

    private Long tagId;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        if (month!= null && month.length() == 1){
            return "0" + month;
        }
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    private String year;
    private String month;
}
