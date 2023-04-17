package com.vpsair.modules.pan.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 发起分享信息 DTO
 * @author Shen
 * @email syf0412@qq.com
 * @date 2021-10-30 23:06:48
 */
@Data
@ApiModel("获取分享信息")
public class RequestShareDTO {

	/**
	 * 文件夹/文件id
	 */
	@ApiModelProperty("文件夹/文件id")
	private Long fid;
	/**
	 * 类型：文件|文件夹
	 */
	@ApiModelProperty("类型：文件|文件夹")
	private Integer type;
	/**
	 * 分享类型：私密|公开
	 */
	@ApiModelProperty("分享类型：私密|公开")
	private String shareType;
	/**
	 * 过期类型
	 */
	@ApiModelProperty("过期类型：0永不过期；1一天；2一周；3一个月")
	private Integer expireType;
	/**
	 * 限制下载次数
	 */
	@ApiModelProperty("限制下载次数：0为不限制")
	private Integer downloadLimit;
	/**
	 * 备注
	 */
	@ApiModelProperty("备注")
	private String remark;
}
