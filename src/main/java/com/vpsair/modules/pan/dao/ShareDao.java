package com.vpsair.modules.pan.dao;

import com.vpsair.modules.pan.entity.ShareEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 分享信息 DAO
 * @author Shen
 * @email syf0412@qq.com
 * @date 2021-10-30 23:06:48
 */
@Mapper
public interface ShareDao extends BaseMapper<ShareEntity> {
	
}
