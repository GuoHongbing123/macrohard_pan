package com.vpsair.modules.pan.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 网盘用户 实体类
 * @author Shen
 * @email syf0412@qq.com
 * @date 2021-10-30 23:15:10
 */
@Data
@TableName("pan_userinfo")
public class UserinfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 系统用户id
	 */
	private Long sysId;
	/**
	 * 用户名（唯一键）
	 */
	private String username;
	/**
	 * 昵称
	 */
	private String nickname;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 手机
	 */
	private String mobile;
	/**
	 * 邮箱
	 */
	private String mail;
	/**
	 * 头像
	 */
	private String avatar;
	/**
	 * 空间限制 | B
	 */
	private Long sizeLimit;
	/**
	 * 已使用空间 | B
	 */
	private Long totalSize;
	/**
	 * 最后登录IP
	 */
	private String lastLoginIp;
	/**
	 * 最后登录时间
	 */
	private Date lastLoginTime;
	/**
	 * 状态：1超级管理员 2管理员 3注册用户 4封禁用户
	 */
	private Integer status;
	/**
	 * 网盘根目录Id
	 */
	private String remark;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;

}
