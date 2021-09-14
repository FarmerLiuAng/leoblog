package com.mashenzhilu.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mashenzhilu.blog.dao.dos.Archives;
import com.mashenzhilu.blog.dao.pojo.Article;

import java.util.List;


public interface ArticleMapper extends BaseMapper<Article> {

    List<Article> hotArticle(int limit);

    List<Article> newArticle(int limit);

    List<Archives> listArchives();
//    List<Article> newArticle(int limit);

    IPage<Article> listArticle(Page<Article> page,
                               Long categoryId,
                               Long tagId,
                               String year,
                               String month);
}
