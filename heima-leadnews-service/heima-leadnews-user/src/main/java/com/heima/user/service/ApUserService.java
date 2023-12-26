package com.heima.user.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.user.dtos.LoginDto;

public interface ApUserService {
    /**
     * app端登录功能
     * @param dto
     * @return
     */
    ResponseResult login(LoginDto dto);
}
