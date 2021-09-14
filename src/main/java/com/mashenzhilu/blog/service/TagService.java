package com.mashenzhilu.blog.service;

import com.mashenzhilu.blog.vo.Result;
import com.mashenzhilu.blog.vo.TagVo;

import java.util.List;

public interface TagService {

    List<TagVo> findTagsByArticleId(Long articleId);

    Result hots(int limit);

    Result findAll();

    Result findAllDetail();

    Result findTagListById(Long id);
}
