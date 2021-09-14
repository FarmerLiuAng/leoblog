package com.mashenzhilu.blog.vo;

//import com.mszlu.blog.dao.pojo.ArticleBody;
//import com.mszlu.blog.dao.pojo.Category;
//import com.mszlu.blog.dao.pojo.SysUser;
//import com.mszlu.blog.dao.pojo.Tag;


import lombok.Data;

import java.util.List;

@Data
public class ArticleVo {

//    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getCommentCounts() {
        return commentCounts;
    }

    public void setCommentCounts(int commentCounts) {
        this.commentCounts = commentCounts;
    }

    public int getViewCounts() {
        return viewCounts;
    }

    public void setViewCounts(int viewCounts) {
        this.viewCounts = viewCounts;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<TagVo> getTags() {
        return tags;
    }

    public void setTags(List<TagVo> tags) {
        this.tags = tags;
    }

    private String title;

    private String summary;

    private int commentCounts;

    private int viewCounts;

    private int weight;
    /**
     * 创建时间
     */
    private String createDate;

    private String author;

    public ArticleBodyVo getBody() {
        return body;
    }

    public void setBody(ArticleBodyVo body) {
        this.body = body;
    }

    public List<CategoryVo> getCategorys() {
        return categorys;
    }

    public void setCategorys(List<CategoryVo> categorys) {
        this.categorys = categorys;
    }

    private ArticleBodyVo body;

    private List<TagVo> tags;

    private List<CategoryVo> categorys;

    public CategoryVo getCategory() {
        return category;
    }

    public void setCategory(CategoryVo category) {
        this.category = category;
    }

    private CategoryVo category;


}
