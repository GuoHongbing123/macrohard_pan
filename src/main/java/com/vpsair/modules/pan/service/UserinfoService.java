package com.vpsair.modules.pan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vpsair.common.utils.PageUtils;
import com.vpsair.common.utils.PageDTO;
import com.vpsair.common.utils.Result;
import com.vpsair.modules.pan.dto.userinfo.*;
import com.vpsair.modules.pan.entity.UserinfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 网盘用户 接口层
 * @author Shen
 * @email syf0412@qq.com
 * @date 2021-10-30 23:15:10
 */
public interface UserinfoService extends IService<UserinfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取分页
     */
    PageDTO<UserinfoEntity> getPage(Integer page, Integer limit);
    /**
     * 注册
     */
    Result<UserinfoDTO> register(RegisterDTO registerDTO);
    /**
     * 加密
     */
    String handlePassword(String password);
    /**
     * 登录
     */
    Result<Map<String,Object>> login(LoginDTO loginDTO);
    /**
     * 更新信息
     */
    int update(Long sysId, UpdateDTO updateDTO);
    /**
     * 修改密码
     */
    int changePassword(Long userId, ChangePasswordDTO changePasswordDTO);
    /**
     * 忘记密码
     */
    int forgetPassword(ChangePasswordDTO changePasswordDTO);
    /**
     * 根据sysid获取用户信息
     */
    UserinfoEntity getBySysId(Long sysId);
    /**
     * 根据用户获取总容量
     */
    Long getTotalSize(Long userId);
}

