package com.mashenzhilu.blog.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("upload")
public class UploadController {

//    @PostMapping
//    public Result upload(@RequestParam("image") MultipartFile file){
//        //原始文件的名称
//        String originalFilename = file.getOriginalFilename();
//        //uuid随机字符串获得唯一文件名称
//        String filename =
//                UUID.randomUUID().toString()+ "." +
//                        StringUtils.substringAfterLast(originalFilename, ".");
//
//        //上传文件
//
//    }
}
