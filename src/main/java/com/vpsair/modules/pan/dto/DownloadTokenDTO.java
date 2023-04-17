package com.vpsair.modules.pan.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("下载")
public class DownloadTokenDTO {

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("标识")
    private String fileKey;

    @ApiModelProperty("真实名称")
    private String realName;

    @ApiModelProperty("真实路径")
    private String fullPath;

    @ApiModelProperty("token")
    private String token;

    @ApiModelProperty("过期时间")
    private Date expireDate;

    @ApiModelProperty("创建时间")
    private Date creatTime;

//    public String getFullPath() {
//        return fullPath;
//    }
//
//    public String getRealName() {
//        return realName;
//    }
}
