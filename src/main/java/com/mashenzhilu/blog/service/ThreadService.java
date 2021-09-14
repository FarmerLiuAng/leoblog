package com.mashenzhilu.blog.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mashenzhilu.blog.dao.mapper.ArticleMapper;
import com.mashenzhilu.blog.dao.pojo.Article;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {
    //在线程池中进行
    @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) throws InterruptedException {
        int viewCounts = article.getViewCounts();
        Article articleUpdated = new Article();
//        articleUpdated.setViewCounts(viewCounts + 1);
        BeanUtils.copyProperties(article, articleUpdated);
        articleUpdated.setViewCounts(viewCounts + 1);
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId, article.getId());
        //目的是为了保证在多线程环境下的线程安全
        //update article set viewcopunt = 100 where id = id and viewcount = 99
        updateWrapper.eq(Article::getViewCounts, viewCounts);
        articleMapper.update(articleUpdated, updateWrapper);
        try{
            Thread.sleep(5000);
            System.out.println("修改成功");
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
