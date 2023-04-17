package com.vpsair.modules.pan.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 文件 实体类
 * @author Shen
 * @email syf0412@qq.com
 * @date 2021-10-30 21:42:21
 */
@Data
@TableName("pan_file")
public class FileEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 外部存储id
	 */
	private Long foreignId;
	/**
	 * 文件夹id
	 */
	private Long folderId;
	/**
	 * 文件名称
	 */
	private String realName;
	/**
	 * 全路径
	 */
	private String fullPath;
	/**
	 * 真实路径
	 */
	private String realPath;
	/**
	 * 文件大小|B
	 */
	private Long size;
	/**
	 * 文件后缀
	 */
	private String suffix;
	/**
	 * 排序
	 */
	private String sort;
	/**
	 * 已上传分片
	 */
	private Integer shardIndex;
	/**
	 * 分片大小|B
	 */
	private Long shardSize;
	/**
	 * 分片总数
	 */
	private Integer shardTotal;
	/**
	 * 视频封面
	 */
	private String thumb;
	/**
	 * 文件标识
	 */
	private String fileKey;
	/**
	 * 所属用户
	 */
	private Long ownerId;
	/**
	 * 所属用户名
	 */
	private String ownerName;
	/**
	 * 所属用户类型（冗余字段）
	 */
	private String ownerType;
	/**
	 * 是否分享
	 */
	private Integer share;
	/**
	 * 分享ID
	 */
	private Integer shareId;
	/**
	 * 冗余字段
	 */
	private String type;
	/**
	 * 状态：1正常 2隐藏 3历史版本 4封禁 5禁止下载 6损坏
	 */
	private Integer status;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;

}
