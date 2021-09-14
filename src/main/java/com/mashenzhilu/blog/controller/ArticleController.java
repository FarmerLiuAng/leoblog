package com.mashenzhilu.blog.controller;

import com.mashenzhilu.blog.service.ArticleService;
import com.mashenzhilu.blog.vo.Result;
import com.mashenzhilu.blog.vo.params.ArticleParam;
import com.mashenzhilu.blog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;


    //Result是统一结果返回
    @PostMapping
//    @Cache(expire = 5 * 60 * 1000, name = "listArticle")
    public Result listArticle(@RequestBody PageParams pageParams) {
        //ArticleVo 页面接收的数据
        return articleService.listArticle(pageParams);
    }

    @PostMapping("hot")
    public Result hotArticle() {
        //需要返回最热的几个文章
        int limit = 3;
        return articleService.hotArticle(limit);

    }

    @PostMapping("new")
    public Result newArticle(){
        int limit = 3;
        return  articleService.newArticle(limit);
    }


    @PostMapping("listArchives")
    public Result listArchives(){

        return  articleService.listArchives();
    }


    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long articleId){
        return articleService.findArticleById(articleId);
    }


    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam, @RequestHeader("Authorization") String token){
        return articleService.publish(articleParam, token);
    }

}
