package com.mashenzhilu.blog.service.imp;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mashenzhilu.blog.dao.dos.Archives;
import com.mashenzhilu.blog.dao.mapper.ArticleBodyMapper;
import com.mashenzhilu.blog.dao.pojo.*;
import com.mashenzhilu.blog.service.SysUserService;
import com.mashenzhilu.blog.dao.mapper.ArticleMapper;
import com.mashenzhilu.blog.dao.mapper.ArticleTagMapper;
import com.mashenzhilu.blog.dao.mapper.CategoryMapper;
import com.mashenzhilu.blog.dao.pojo.*;
import com.mashenzhilu.blog.service.ArticleService;
import com.mashenzhilu.blog.service.TagService;
import com.mashenzhilu.blog.service.ThreadService;
import com.mashenzhilu.blog.vo.*;
import com.mashenzhilu.blog.vo.*;
import com.mashenzhilu.blog.vo.params.ArticleParam;
import com.mashenzhilu.blog.vo.params.PageParams;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImp implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private TagService tagService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Autowired
    private ThreadService threadService;


//    @Override
//    public Result listArticle(PageParams pageParams) {
//        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPagesize());
//        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//
//        if (pageParams.getCategoryId()!=null){
//            queryWrapper.eq(Article::getCategoryId, pageParams.getCategoryId());
//        }
//        ArrayList<Long> tagsIds = new ArrayList<>();
//        if (pageParams.getTagId()!=null){
//            LambdaQueryWrapper<ArticleTag> queryWrapper1 = new LambdaQueryWrapper<>();
//            queryWrapper1.eq(ArticleTag::getTagId, pageParams.getTagId());
//            List<ArticleTag> articleTags = articleTagMapper.selectList(queryWrapper1);
//            for (ArticleTag articletag: articleTags
//                 ) {
//                tagsIds.add(articletag.getArticleId());
//            }
//            queryWrapper.in(Article::getId, tagsIds);
//
//
//        }
//        //根据权重进行置顶排序Article::getWeight
//        //根据时间进行排序getCreateDate
//        queryWrapper.orderByDesc(Article::getWeight, Article::getCreateDate);
//        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
////        System.out.println(articlePage);
//        List<Article> records = articlePage.getRecords();
//        List<ArticleVo> articleVoList = copyList(records, true, true);
//        return Result.success(articleVoList);
//    }

    @Override
    public Result listArticle(PageParams pageParams) {

        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPagesize());
        IPage<Article> articleIPage =
                this.articleMapper.listArticle(
                        page,pageParams.getCategoryId(),
                        pageParams.getTagId(),
                        pageParams.getYear(),
                        pageParams.getMonth());
        return Result.success(copyList(articleIPage.getRecords(),true,true));

    }

    @Override
    public Result hotArticle(int limit) {
        //通过view_conuts排序
        List<Article> hotArticles = articleMapper.hotArticle(limit);
        return Result.success(copyList(hotArticles, false, false));

    }

    @Override
    public Result newArticle(int limit) {
        //通过view_conuts排序
        List<Article> hotArticles = articleMapper.newArticle(limit);
        return Result.success(copyList(hotArticles, false, false));

    }

    @Override
    public Result listArchives() {
        List<Archives> articleList = articleMapper.listArchives();
        return Result.success(articleList);

    }

    @Override
    public Result findArticleById(Long articleId) {
        /*
        1.根据id查询文章信息
        2.关联查询
         */
        Article article = articleMapper.selectById(articleId);
//        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Article::getId, articleId);
//        Article article = articleMapper.selectOne(queryWrapper);
        ArticleVo articleVo = copy(article, true, true, true, true);
        try {
            threadService.updateArticleViewCount(articleMapper, article);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.success(articleVo);
    }

    @Override
    public Result publish(ArticleParam articleParam, String token) {
        String userJSON = redisTemplate.opsForValue().get(token);
        SysUser sysUser = JSON.parseObject(userJSON, SysUser.class);

        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        article.setCategoryId(articleParam.getCategory().getId());
        article.setCreateDate(System.currentTimeMillis());
        article.setCommentCounts(0);
        article.setSummary(articleParam.getSummary());
        article.setTitle(articleParam.getTitle());
        article.setViewCounts(0);
        article.setWeight(Article.Article_Common);
        article.setBodyId(-1L);
        //主键自增，插入之后会将主键返回到原对象中
        articleMapper.insert(article);

        //tags
        List<TagVo> tags = articleParam.getTags();
        if (tags != null) {
            for (TagVo tag : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tag.getId());
                articleTagMapper.insert(articleTag);
            }
        }
        ArticleBody articleBody = new ArticleBody();
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBody.setArticleId(article.getId());
        articleBodyMapper.insert(articleBody);

        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(article.getId());
        return Result.success(articleVo);
    }




    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
        List<ArticleVo> articleList = new ArrayList<>();
        for (Article article: records
             ) {
            articleList.add(copy(article,true, true,false,false));
        }
        return  articleList;
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody,boolean isCategory) {
        List<ArticleVo> articleList = new ArrayList<>();
        for (Article article: records
        ) {
            articleList.add(copy(article,isTag, isAuthor,isAuthor,isAuthor));
        }
        return  articleList;
    }


    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody,
                           boolean isCategory){
        ArticleVo articleVo= new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);
        articleVo.setCreateDate(new org.joda.time.DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //为了将article的日期转化为中文形式
        if (isTag){ //标志是否有Tag
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if (isAuthor){ //是否有作者
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname()); //getnickname 作者名称
        }

        if (isBody){
            Long bodyId = article.getBodyId();
            articleVo.setBody(findBodyById(bodyId));
        }

        if (isCategory){
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(findCategoryId(categoryId));
        }

        return articleVo;
    }

    private CategoryVo findCategoryId(Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category, categoryVo);
        return categoryVo;
    }

    private ArticleBodyVo findBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());

        return articleBodyVo;
    }


}
