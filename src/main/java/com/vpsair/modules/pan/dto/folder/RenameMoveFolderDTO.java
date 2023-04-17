package com.vpsair.modules.pan.dto.folder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 文件夹 DTO
 * @author Shen
 * @email syf0412@qq.com
 * @date 2021-10-30 21:42:21
 */
@Data
@ApiModel("重命名移动文件夹")
public class RenameMoveFolderDTO {

	/**
	 * id
	 */
	@ApiModelProperty("id")
	private Long id;
	/**
	 * 父文件夹id
	 */
	@ApiModelProperty("父文件夹id")
	private Long pid;
	/**
	 * 文件夹名称（默认根目录'/'）
	 */
	@ApiModelProperty("文件夹名称（默认根目录''）")
	private String realName;

}
