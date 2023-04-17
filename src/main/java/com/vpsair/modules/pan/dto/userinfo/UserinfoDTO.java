package com.vpsair.modules.pan.dto.userinfo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 网盘用户 DTO
 * @author Shen
 * @email syf0412@qq.com
 * @date 2021-10-30 23:15:10
 */
@Data
@ApiModel("网盘用户")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserinfoDTO {

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
	 * 手机
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
	 * 空间限制 | B
	 */
	@ApiModelProperty("空间限制 | B")
	private Long sizeLimit;
	/**
	 * 已使用空间 | B
	 */
	@ApiModelProperty("已使用空间 | B")
	private Integer totalSize;
	/**
	 * 最后登录IP
	 */
	@ApiModelProperty("最后登录IP")
	private String lastLoginIp;
	/**
	 * 最后登录时间
	 */
	@ApiModelProperty("最后登录时间")
	private Date lastLoginTime;
	/**
	 * 状态：1超级管理员 2管理员 3注册用户 4封禁用户
	 */
	@ApiModelProperty("状态：1超级管理员 2管理员 3注册用户 4封禁用户")
	private Integer status;
	/**
	 * 备注
	 */
	@ApiModelProperty("备注")
	private String remark;
	/**
	 * 创建时间
	 */
	@ApiModelProperty("创建时间")
	private Date createTime;
	/**
	 * 更新时间
	 */
	@ApiModelProperty("更新时间")
	private Date updateTime;

}
