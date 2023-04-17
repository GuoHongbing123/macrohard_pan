package com.vpsair.modules.oss.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vpsair.modules.oss.entity.SysOssEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件上传
 */
@Mapper
public interface SysOssDao extends BaseMapper<SysOssEntity> {
	
}
