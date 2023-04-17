package com.vpsair.modules.pan.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 移入回收站 DTO
 */
@Data
@ApiModel("移入回收站")
public class RequestRecycleBinDTO {

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
}
