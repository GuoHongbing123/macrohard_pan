package com.vpsair.modules.pan.dto.userinfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 更新信息 DTO
 * @author Shen
 * @email syf0412@qq.com
 * @date 2021-10-30 23:15:10
 */
@Data
@ApiModel("修改密码")
public class ChangePasswordDTO {
	/**
	 * 用户名
	 */
	@ApiModelProperty("用户名")
	private String username;
	/**
	 * 邮箱
	 */
	@ApiModelProperty("邮箱")
	private String mail;
	/**
	 * 密码
	 */
	@ApiModelProperty("密码")
	private String password;
	/**
	 * 新密码
	 */
	@ApiModelProperty("新密码")
	private String newPassword;
}
