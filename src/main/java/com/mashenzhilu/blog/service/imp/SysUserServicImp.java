package com.mashenzhilu.blog.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mashenzhilu.blog.service.LoginService;
import com.mashenzhilu.blog.service.SysUserService;
import com.mashenzhilu.blog.dao.mapper.SysuserMapper;
import com.mashenzhilu.blog.dao.pojo.SysUser;
import com.mashenzhilu.blog.vo.ErrorCode;
import com.mashenzhilu.blog.vo.LoginUserVo;
import com.mashenzhilu.blog.vo.Result;
import com.mashenzhilu.blog.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServicImp implements SysUserService {

    @Autowired
    private SysuserMapper sysuserMapper;

    @Autowired
    private LoginService loginService;

    //加密盐
    private static final String slat = "mszlu!@#";
    @Override
    public SysUser findUserById(Long Id) {

        SysUser sysUser = sysuserMapper.selectById(Id);
        if (sysUser == null){
            sysUser = new SysUser();
            sysUser.setNickname("无名氏");
        }
        return sysUser;
    }

    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper =
                new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount, account);
        queryWrapper.eq(SysUser::getPassword, password);
        queryWrapper.select(SysUser::getAccount,SysUser::getId,
                SysUser::getAvatar,SysUser::getNickname);
        return sysuserMapper.selectOne(queryWrapper);
    }

    @Override
    public Result findUserByToken(String token) {

        /**
         * 1.token 合法性校验
         * 是否为空 解析是否成功 redis中是否存在
         * 2.如果失败，返回result.fail
         * 3.成功返回LoginUserVo对象
         */

        if (token.equals(null)){
            return Result.fail(ErrorCode.TOKRN_ILLEGAL.getCode(),
                    ErrorCode.TOKRN_ILLEGAL.getMsg());
        }

        SysUser sysUser = loginService.checkToken(token);

        if (sysUser == null){
            return Result.fail(ErrorCode.TOKRN_ERROR.getCode(),
                    ErrorCode.TOKRN_ERROR.getMsg());
        }
        LoginUserVo loginUserVo = new LoginUserVo();
        BeanUtils.copyProperties(sysUser, loginUserVo);
        return Result.success(loginUserVo);
    }

    @Override
    public boolean checkExist(String account) {

        LambdaQueryWrapper<SysUser> queryWrapper =
                new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount, account).last("limit 1");;
        SysUser sysUser = sysuserMapper.selectOne(queryWrapper);
        return (sysUser != null);
    }

    @Override
    public void insertUser(SysUser sysUser) {
         sysuserMapper.insert(sysUser);
    }

    @Override
    public UserVo findUserVoById(Long authorId) {
        SysUser sysUser = sysuserMapper.selectById(authorId);
        if (sysUser == null){
            sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
            sysUser.setNickname("码神之路");
        }
        UserVo userVo = new UserVo();
        userVo.setAvatar(sysUser.getAvatar());
        userVo.setNickname(sysUser.getNickname());
        userVo.setId(sysUser.getId());
        return userVo;
    }
}
