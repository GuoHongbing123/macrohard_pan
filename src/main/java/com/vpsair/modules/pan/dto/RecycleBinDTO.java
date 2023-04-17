package com.vpsair.modules.pan.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 回收站 DTO
 * @author Shen
 * @email syf0412@qq.com
 * @date 2021-10-30 23:06:48
 */
@Data
@ApiModel("回收站")
public class RecycleBinDTO {

	/**
	 * id
	 */
	@ApiModelProperty("id")
	private Long id;
	/**
	 * 文件/文件夹id
	 */
	@ApiModelProperty("文件/文件夹id")
	private Long fid;
	/**
	 * 类型：文件|文件夹
	 */
	@ApiModelProperty("类型：文件|文件夹")
	private Integer type;
	/**
	 * 文件夹/文件
	 */
	@ApiModelProperty("文件夹/文件")
	private String realName;
	/**
	 * 原文件路径
	 */
	@ApiModelProperty("原文件路径")
	private String fullPath;
	/**
	 * 文件大小|B
	 */
	@ApiModelProperty("文件大小|B")
	private Long size;
	/**
	 * 所属用户
	 */
	@ApiModelProperty("所属用户")
	private Long ownerId;
	/**
	 * 所属用户名
	 */
	@ApiModelProperty("所属用户名")
	private String ownerName;
	/**
	 * 过期时间
	 */
	@ApiModelProperty("过期时间")
	private Date expireDate;
	/**
	 * 状态
	 */
	@ApiModelProperty("状态")
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
