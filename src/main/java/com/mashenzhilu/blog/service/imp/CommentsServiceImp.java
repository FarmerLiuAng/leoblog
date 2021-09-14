package com.mashenzhilu.blog.service.imp;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mashenzhilu.blog.service.CommentsService;
import com.mashenzhilu.blog.service.SysUserService;
import com.mashenzhilu.blog.vo.CommentVo;
import com.mashenzhilu.blog.vo.Result;
import com.mashenzhilu.blog.vo.UserVo;
import com.mashenzhilu.blog.vo.params.CommentParam;
import com.mashenzhilu.blog.dao.mapper.CommentMapper;
import com.mashenzhilu.blog.dao.pojo.Comment;
import com.mashenzhilu.blog.dao.pojo.SysUser;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentsServiceImp implements CommentsService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SysUserService sysUserService;
    @Override
    public Result commentsByArticleId(Long id) {
        /*
        1.根据文章id 查询评论列表
        2.根据作者id获取作者信息
        3.判断 如果level = 1 是否有子评论
        4.查询子评论
         */
        LambdaQueryWrapper<Comment> queryWrapper= new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getLevel, 1)
                .eq(Comment::getArticleId, id);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        List<CommentVo> commentVoList = copyList(comments);
        return Result.success(commentVoList);
    }



    @Override
    public Result comment(CommentParam commentParam, String token) {
        String userJSON = redisTemplate.opsForValue().get(token);
        SysUser sysUser = JSON.parseObject(userJSON, SysUser.class);
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParam.getParent();
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        }else{
            comment.setLevel(2);
        }
        comment.setParentId(parent == null ? 0 : parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        this.commentMapper.insert(comment);
        return Result.success(null);
    }


    public List<CommentVo> copyList(List<Comment> commentList){
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }


    public CommentVo copy(Comment comment){
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment,commentVo);
        //时间格式化
        commentVo.setCreateDate(new DateTime(comment.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        Long authorId = comment.getAuthorId();
        UserVo userVo = sysUserService.findUserVoById(authorId);
        commentVo.setAuthor(userVo);
        //评论的评论
        List<CommentVo> commentVoList = findCommentsByParentId(comment.getId());
        commentVo.setChildrens(commentVoList);
        if (comment.getLevel() > 1) {
            Long toUid = comment.getToUid();
            UserVo toUserVo = sysUserService.findUserVoById(toUid);
            commentVo.setToUser(toUserVo);
        }
        return commentVo;
    }

    private List<CommentVo> findCommentsByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId,id);
        queryWrapper.eq(Comment::getLevel,2);
        List<Comment> comments = this.commentMapper.selectList(queryWrapper);
        return copyList(comments);
    }




}
