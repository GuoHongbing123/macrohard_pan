package com.vpsair.modules.pan.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@TableName("pan_download_token")
public class DownloadTokenEntity {

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

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Date getExpireDate() {
//        return expireDate;
//    }
//
//    public String getToken() {
//        return token;
//    }
//
//    public void setToken(String token) {
//        this.token = token;
//    }
//
//    public void setExpireDate(Date expireDate) {
//        this.expireDate = expireDate;
//    }
//
//    public void setFullPath(String fullPath) {
//        this.fullPath = fullPath;
//    }
//
//    public void setRealName(String realName) {
//        this.realName = realName;
//    }
//
//    public void setFileKey(String fileKey) {
//        this.fileKey = fileKey;
//    }
//
//    public void setCreatTime(Date creatTime) {
//        this.creatTime = creatTime;
//    }
//
//    public String getFileKey() {
//        return fileKey;
//    }
}
