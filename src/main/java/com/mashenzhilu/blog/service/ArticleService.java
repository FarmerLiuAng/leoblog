package com.mashenzhilu.blog.service;

import com.mashenzhilu.blog.vo.Result;
import com.mashenzhilu.blog.vo.params.ArticleParam;
import com.mashenzhilu.blog.vo.params.PageParams;

public interface ArticleService  {
/*
 * 分页查询 文章列表
 * @author 
 * @date 07 九月 2021
 * 
 * @param pageParams
 * @return
 **/
   Result listArticle(PageParams pageParams);

   Result hotArticle(int limit);

   Result newArticle(int limit);

   //文章归档
   Result listArchives();

    Result findArticleById(Long articleId);

   Result publish(ArticleParam articleParam,String token);

}
