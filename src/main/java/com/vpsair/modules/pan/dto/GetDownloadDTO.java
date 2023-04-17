package com.vpsair.modules.pan.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("获取下载")
public class GetDownloadDTO {


    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("token")
    private String token;

    @ApiModelProperty("下载时间")
    private String dlLink;

    @ApiModelProperty("创建时间")
    private Date creatTime;

    @ApiModelProperty("过期时间")
    private Date expireDate;

//    public void setExpireDate(Date expireDate) { this.expireDate = expireDate; }
//
//
//
//    public void setId(Long id) { this.id = id; }
//
//
//
//    public void setToken(String token) { this.token = token; }
//
//    public Long getId() { return id; }
//
//    public String getToken() { return token; }
//
//    public Date getCreatTime() { return creatTime; }
//
//    public void setCreatTime(Date creatTime) { this.creatTime = creatTime; }
//
//    public String getDlLink() { return dlLink; }
//
//    public void setDlLink(String dlLink) { this.dlLink = dlLink; }
}
