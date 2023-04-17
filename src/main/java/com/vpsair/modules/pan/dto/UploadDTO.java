package com.vpsair.modules.pan.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 上传DTO
 */
@Data
@ApiModel("上传DTO")
public class UploadDTO {

        @ApiModelProperty("文件名")
        private String name;
        @ApiModelProperty("网盘目录id")
        private Long folderId;
        @ApiModelProperty("唯一key")
        private String key;
        @ApiModelProperty("文件后绷")
        private String suffix;
        @ApiModelProperty("文件分片序号")
        private Integer shardIndex;
        @ApiModelProperty("文件分片总数")
        private Integer shardTotal;
        @ApiModelProperty("文件总大小")
        private Long size;
        @ApiModelProperty("文件分片大小")
        private Long shardSize;
        @ApiModelProperty("文件分片base64")
        private String shard;





}
