package com.vpsair.modules.pan.dao;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vpsair.modules.pan.entity.DownloadTokenEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface DownloadTokenDao extends BaseMapper<DownloadTokenEntity> {

    DownloadTokenEntity getByFileKey(String fileKey);

    DownloadTokenEntity getById(Long id, Date date);

    int removeExpireToken(Date date);

}
