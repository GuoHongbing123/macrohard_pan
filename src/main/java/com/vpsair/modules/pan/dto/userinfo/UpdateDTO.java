package com.vpsair.modules.pan.dto.userinfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 更新信息 DTO
 * @author Shen
 * @email syf0412@qq.com
 * @date 2021-10-30 23:15:10
 */
@Data
@ApiModel("更新")
public class UpdateDTO {
	/**
	 * 昵称
	 */
	@ApiModelProperty("昵称")
	private String nickname;
	/**
	 * 密码
	 */
	@ApiModelProperty("手机")
	private String mobile;
	/**
	 * 邮箱
	 */
	@ApiModelProperty("邮箱")
	private String mail;
	/**
	 * 头像
	 */
	@ApiModelProperty("头像")
	private String avatar;
	/**
	 * 备注
	 */
	@ApiModelProperty("备注")
	private String remark;
}
