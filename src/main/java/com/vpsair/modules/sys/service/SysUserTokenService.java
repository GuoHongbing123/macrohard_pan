package com.vpsair.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vpsair.common.utils.R;
import com.vpsair.modules.sys.entity.SysUserTokenEntity;

/**
 * 用户Token
 */
public interface SysUserTokenService extends IService<SysUserTokenEntity> {

	/**
	 * 生成token
	 * @param userId  用户ID
	 */
	R createToken(long userId);
	String getToken(Long userId);
	/**
	 * 退出，修改token值
	 * @param userId  用户ID
	 */
	void logout(long userId);

}
