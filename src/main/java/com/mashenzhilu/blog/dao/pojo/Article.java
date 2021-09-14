package com.mashenzhilu.blog.dao.pojo;

import lombok.Data;

@Data
public class Article {

    public static int getArticle_TOP() {
        return Article_TOP;
    }

    public static int getArticle_Common() {
        return Article_Common;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public int getCommentCounts() {
        return commentCounts;
    }

    public int getViewCounts() {
        return viewCounts;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public Long getBodyId() {
        return bodyId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public int getWeight() {
        return weight;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public static final int Article_TOP = 1;

    public static final int Article_Common = 0;

    private Long id;

    private String title;

    private String summary;

    private int commentCounts;

    private int viewCounts;

    /**
     * 作者id
     */
    private Long authorId;
    /**
     * 内容id
     */
    private Long bodyId;
    /**
     *类别id
     */
    private Long categoryId;

    /**
     * 置顶
     */
    private int weight = Article_Common;


    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setCommentCounts(int commentCounts) {
        this.commentCounts = commentCounts;
    }

    public void setViewCounts(int viewCounts) {
        this.viewCounts = viewCounts;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public void setBodyId(Long bodyId) {
        this.bodyId = bodyId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    /**
     * 创建时间
     */
    private Long createDate;
}