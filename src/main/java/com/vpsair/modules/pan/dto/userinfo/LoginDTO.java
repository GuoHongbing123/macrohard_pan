package com.vpsair.modules.pan.dto.userinfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 注册DTO
 * @author Shen
 * @email syf0412@qq.com
 * @date 2021-10-30 23:15:10
 */
@Data
@ApiModel("登录")
public class LoginDTO {
	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long id;
	/**
	 * 系统用户id
	 */
	@ApiModelProperty("系统用户id")
	private Long sysId;
	/**
	 * 用户名（唯一键）
	 */
	@ApiModelProperty("用户名（唯一键）")
	private String username;
	/**
	 * 昵称
	 */
	@ApiModelProperty("昵称")
	private String nickname;
	/**
	 * 密码
	 */
	@ApiModelProperty("密码")
	private String password;
	/**
	 * 手机号
	 */
	@ApiModelProperty("手机号")
	private String mobile;
	/**
	 * 邮箱
	 */
	@ApiModelProperty("邮箱")
	private String mail;
}
