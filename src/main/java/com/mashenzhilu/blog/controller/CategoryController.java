package com.mashenzhilu.blog.controller;

import com.mashenzhilu.blog.service.CategoryService;
import com.mashenzhilu.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("categorys")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;



    @GetMapping
    public Result listCategory() {
        return categoryService.findAll();
    }

    @GetMapping("detail")
    public Result catrgoryDetail(){
        return categoryService.findAllDetail();
    }

    @GetMapping("detail/{id}")
    public Result catrgoryList(@PathVariable("id") Long id){
        return categoryService.findCategoryListById(id);
    }
}