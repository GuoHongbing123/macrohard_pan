package com.vpsair.modules.pan.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 分享信息 DTO
 * @author Shen
 * @email syf0412@qq.com
 * @date 2021-10-30 23:06:48
 */
@Data
@ApiModel("保存信息")
public class SaveDTO {

	/**
	 * 文件夹id
	 */
	@ApiModelProperty("文件夹id")
	private Long folderId;
	/**
	 * 文件id
	 */
	@ApiModelProperty("文件id")
	private Long fileId;
	/**
	 * 目标文件夹id
	 */
	@ApiModelProperty("目标文件夹id")
	private Long targetFolderId;
}
