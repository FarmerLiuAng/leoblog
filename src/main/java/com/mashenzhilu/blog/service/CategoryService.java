package com.mashenzhilu.blog.service;

import com.mashenzhilu.blog.vo.Result;

public interface CategoryService {
    Result findAll();

    Result findAllDetail();

    Result findCategoryListById(Long id);
}
