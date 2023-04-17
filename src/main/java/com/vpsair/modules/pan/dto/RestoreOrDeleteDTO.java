package com.vpsair.modules.pan.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 还原或删除文件&文件夹 DTO
 * @date 2021-10-30 23:06:48
 */
@Data
@ApiModel("还原或删除文件&文件夹")
public class RestoreOrDeleteDTO {
    /**
     * 回收站id
     */
    @ApiModelProperty("回收站id")
    private Long id;
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
