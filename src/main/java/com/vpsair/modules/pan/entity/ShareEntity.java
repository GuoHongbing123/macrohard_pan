package com.vpsair.modules.pan.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 分享信息 实体类
 * @author Shen
 * @email syf0412@qq.com
 * @date 2021-10-30 23:06:48
 */
@Data
@TableName("pan_share")
public class ShareEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 文件夹/文件id
	 */
	private Long fid;
	/**
	 * 类型：文件|文件夹
	 */
	private Integer type;
	/**
	 * 分享类型：私密|公开
	 */
	private String shareType;
	/**
	 * 分享路径
	 */
	private String sharePath;
	/**
	 * 文件夹/文件名称
	 */
	private String realName;
	/**
	 * 提取码
	 */
	private String code;
	/**
	 * 所属用户/分享人id
	 */
	private Long ownerId;
	/**
	 * 所属用户/分享人
	 */
	private String ownerName;
	/**
	 * 过期类型
	 */
	private Integer expireType;
	/**
	 * 过期时间
	 */
	private Date expireDate;
	/**
	 * 下载次数
	 */
	private Integer downloadNum;
	/**
	 * 限制下载次数
	 */
	private Integer downloadLimit;
	/**
	 * 状态：0永久 1正常 2过期 3次数用尽 4违规
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
