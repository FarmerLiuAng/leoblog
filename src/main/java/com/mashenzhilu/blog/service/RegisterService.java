package com.mashenzhilu.blog.service;

import com.mashenzhilu.blog.vo.Result;
import com.mashenzhilu.blog.vo.params.RegisterParam;
import org.springframework.transaction.annotation.Transactional;

//加入事务，可以保证回滚
@Transactional
public interface RegisterService {
    Result register(RegisterParam registerParam);
}
