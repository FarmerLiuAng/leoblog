package com.mashenzhilu.blog.service.imp;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.mashenzhilu.blog.dao.pojo.SysUser;
import com.mashenzhilu.blog.service.SysUserService;
import com.mashenzhilu.blog.utils.JWTUtils;
import com.mashenzhilu.blog.vo.ErrorCode;
import com.mashenzhilu.blog.vo.Result;
import com.mashenzhilu.blog.service.RegisterService;
import com.mashenzhilu.blog.vo.params.RegisterParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.concurrent.TimeUnit;

@Service
public class RegisterServiceImp implements RegisterService {
    private static final String slat = "mszlu!@#";

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Result register(RegisterParam registerParam) {
        String account = registerParam.getAccount();
        String password = registerParam.getPassword();
        String nickname = registerParam.getNickname();

        /*
        1.检查上面是否为空
        2.检查此用户是否存在
        3.向数据库中插入该用户
        4.通过id建立token，并将token保存，并且返回给浏览器
         */

        if(StringUtils.isBlank(account) || StringUtils.isBlank(password) ||
        StringUtils.isBlank(nickname) ){
            return null;
        }

        boolean isExist = sysUserService.checkExist(account);

        if (isExist){
            return Result.fail(ErrorCode.ACCOUNT_HAS_EXIST.getCode(),
                    ErrorCode.ACCOUNT_HAS_EXIST.getMsg());
        }

        SysUser sysUser = new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        sysUser.setPassword(DigestUtils.md5Hex(password+slat));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        sysUser.setAdmin(1); //1 为true
        sysUser.setDeleted(0); // 0 为false
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        sysUserService.insertUser(sysUser);

        String token = JWTUtils.createToken(sysUser.getId());
        String s = JSON.toJSONString(sysUser);
        redisTemplate.opsForValue().set(token, s, 1, TimeUnit.DAYS);
        return Result.success(token);
    }
}
