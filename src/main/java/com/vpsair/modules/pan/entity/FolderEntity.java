package com.vpsair.modules.pan.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 文件夹 实体类
 * @author Shen
 * @email syf0412@qq.com
 * @date 2021-10-30 21:42:21
 */
@Data
@TableName("pan_folder")
public class FolderEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 父文件夹id
	 */
	private Long pid;
	/**
	 * 文件夹名称（默认根目录'/'）
	 */
	private String realName;
	/**
	 * 文件夹全路径（默认根目录'/'）
	 */
	private String fullPath;
	/**
	 * 排序
	 */
	private String sort;
	/**
	 * 是否为空
	 */
	private Integer isEmpty;
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
	 * 分享id
	 */
	private Long shareId;
	/**
	 * 冗余字段
	 */
	private String type;
	/**
	 * 状态：1正常 2隐藏 4封禁
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
