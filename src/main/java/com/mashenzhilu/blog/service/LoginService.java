package com.mashenzhilu.blog.service;

import com.mashenzhilu.blog.vo.params.LoginParam;
import com.mashenzhilu.blog.dao.pojo.SysUser;
import com.mashenzhilu.blog.vo.Result;


public interface LoginService {
    Result login(LoginParam loginParam);

    SysUser checkToken(String token);

    Result logout(String token);
}
