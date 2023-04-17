package com.vpsair.modules.pan.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 文件 DTO
 * @author Shen
 * @email syf0412@qq.com
 * @date 2021-10-30 21:42:21
 */
@Data
@ApiModel("文件")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileDTO {

	/**
	 * id
	 */
	@ApiModelProperty("id")
	private Long id;
	/**
	 * 外部存储id
	 */
	@ApiModelProperty("外部存储id")
	private Long foreignId;
	/**
	 * 文件夹id
	 */
	@ApiModelProperty("文件夹id")
	private Long folderId;
	/**
	 * 文件名称
	 */
	@ApiModelProperty("文件名称")
	private String realName;
	/**
	 * 全路径
	 */
	@ApiModelProperty("全路径")
	private String fullPath;
	/**
	 * 真实路径
	 */
	@ApiModelProperty("真实路径")
	private String realPath;
	/**
	 * 文件大小|B
	 */
	@ApiModelProperty("文件大小|B")
	private Long size;
	/**
	 * 文件后缀
	 */
	@ApiModelProperty("文件后缀")
	private String suffix;
	/**
	 * 排序
	 */
	@ApiModelProperty("排序")
	private String sort;
	/**
	 * 已上传分片
	 */
	@ApiModelProperty("已上传分片")
	private Integer shardIndex;
	/**
	 * 分片大小|B
	 */
	@ApiModelProperty("分片大小|B")
	private Long shardSize;
	/**
	 * 分片总数
	 */
	@ApiModelProperty("分片总数")
	private Integer shardTotal;
	/**
	 * 视频封面
	 */
	@ApiModelProperty("视频封面")
	private String thumb;
	/**
	 * 文件标识
	 */
	@ApiModelProperty("文件标识")
	private String fileKey;
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
	 * 所属用户类型（冗余字段）
	 */
	@ApiModelProperty("所属用户类型（冗余字段）")
	private String ownerType;
	/**
	 * 是否分享
	 */
	@ApiModelProperty("是否分享")
	private Integer share;
	/**
	 * 分享ID
	 */
	@ApiModelProperty("分享ID")
	private Integer shareId;
	/**
	 * 冗余字段
	 */
	@ApiModelProperty("冗余字段")
	private String type;
	/**
	 * 状态：1正常 2隐藏 3历史版本 4封禁 5禁止下载 6损坏
	 */
	@ApiModelProperty("状态：1正常 2隐藏 3历史版本 4封禁 5禁止下载 6损坏")
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
