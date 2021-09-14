package com.mashenzhilu.blog.service.imp;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.mashenzhilu.blog.dao.pojo.SysUser;
import com.mashenzhilu.blog.service.LoginService;
import com.mashenzhilu.blog.service.SysUserService;
import com.mashenzhilu.blog.utils.JWTUtils;
import com.mashenzhilu.blog.vo.ErrorCode;
import com.mashenzhilu.blog.vo.Result;
import com.mashenzhilu.blog.vo.params.LoginParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImp implements LoginService {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //加密盐
    private static final String slat = "mszlu!@#";

    @Override
    public Result login(LoginParam loginParam) {
        /**
         * 1.参数是否合法
         * 2.根据用户名，密码去表中查询
         * 3.不存在则登录失败
         * 4.存在 使用jwt生成token返回前端
         * 5.token放入redis中
         * redis中的token包括user信息和过期时间
         * 登录认证的时候先检查token是否合法，在检查redis中是否过期
         */
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();

        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),
                    ErrorCode.PARAMS_ERROR.getMsg());
        }
        password = DigestUtils.md5Hex(password + slat);

        SysUser sysUser = sysUserService.findUser(account, password);

        if (sysUser == null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),
                    ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }

        String token = JWTUtils.createToken(sysUser.getId());
        String s = JSON.toJSONString(sysUser);
        redisTemplate.opsForValue().set(token, s, 1, TimeUnit.DAYS);

        return Result.success(token);
    }

    @Override
    public SysUser checkToken(String token) {
        if (org.apache.commons.lang3.StringUtils.isBlank(token)){
            return null;
        }
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
        if (stringObjectMap == null){
            return null;
        }
//        String key = token;
        String userJSON = redisTemplate.opsForValue().get(token);

        if (StringUtils.isBlank(userJSON)){
            return null;
        }
        //对json进行解析
        SysUser sysUser = JSON.parseObject(userJSON, SysUser.class);
        return sysUser;
    }

    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_"+token);
        return Result.success(null);
    }
}
