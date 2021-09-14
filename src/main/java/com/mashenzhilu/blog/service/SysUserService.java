package com.mashenzhilu.blog.service;

import com.mashenzhilu.blog.dao.pojo.SysUser;
import com.mashenzhilu.blog.vo.Result;
import com.mashenzhilu.blog.vo.UserVo;

public interface SysUserService {

    SysUser findUserById(Long Id);

    SysUser findUser(String account, String password);

    //根据token查询用户信息
    Result findUserByToken(String token);


    boolean checkExist(String account);

    void insertUser(SysUser sysUser);

    UserVo findUserVoById(Long authorId);
}
