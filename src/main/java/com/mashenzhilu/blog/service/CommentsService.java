package com.mashenzhilu.blog.service;

import com.mashenzhilu.blog.vo.Result;
import com.mashenzhilu.blog.vo.params.CommentParam;

public interface CommentsService {
    Result commentsByArticleId(Long id);

    Result comment(CommentParam commentParam, String token);
}
