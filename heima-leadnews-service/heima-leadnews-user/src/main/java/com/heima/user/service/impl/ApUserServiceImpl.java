package com.heima.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.common.user.dtos.LoginDto;
import com.heima.model.common.user.pojos.ApUser;
import com.heima.user.mapper.ApUserMapper;
import com.heima.user.service.ApUserService;
import com.heima.utils.common.AppJwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class ApUserServiceImpl extends ServiceImpl<ApUserMapper, ApUser> implements ApUserService {

    /**
     * app端登录功能
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult login(LoginDto dto) {
        // 1. 正常登录 用户名和密码
        if (StringUtils.isNoneBlank(dto.getPhone()) && StringUtils.isNoneBlank(dto.getPassword())) {
            // 1.1 根据手机号查询用户信息
            ApUser dbUser = getOne(Wrappers.<ApUser>lambdaQuery().eq(ApUser::getPhone, dto.getPhone()));
            if (dbUser == null) {
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "用户信息不存在");
            }

            // 1.2 比对密码
            String salt = dbUser.getSalt();
            String dtoPassword = dto.getPassword();
            String md5Password = DigestUtils.md5DigestAsHex((dtoPassword + salt).getBytes());
            if (!md5Password.equals(dbUser.getPassword())) {
                return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
            }

            // 1.3 返回数据 jwt user
            String token = AppJwtUtil.getToken(dbUser.getId().longValue());
            Map<String, Object> map = new HashMap<>();
            map.put("token", token);
            dbUser.setSalt("");
            dbUser.setPassword("");
            map.put("user", dbUser);
            return ResponseResult.okResult(map);
        } else {
            // 2. 游客登录
            String token = AppJwtUtil.getToken(0L);
            Map<String, Object> map = new HashMap<>();
            map.put("token", token);

            return ResponseResult.okResult(map);
        }

    }
}
