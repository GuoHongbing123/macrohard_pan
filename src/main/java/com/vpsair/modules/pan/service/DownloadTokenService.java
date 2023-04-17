package com.vpsair.modules.pan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vpsair.common.utils.PageDTO;
import com.vpsair.common.utils.PageUtils;
import com.vpsair.modules.pan.dto.DownloadTokenDTO;
import com.vpsair.modules.pan.dto.GetDownloadDTO;
import com.vpsair.modules.pan.entity.DownloadTokenEntity;

import java.util.Map;

public interface DownloadTokenService extends IService<DownloadTokenEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取分页
     * @return
     */
    PageDTO<DownloadTokenEntity> getPage(Integer page, Integer limit);
    /**
     * 获取下载链接
     */
    GetDownloadDTO generateDLLink(Long id, Integer type);

    DownloadTokenDTO getByToken(Long id, String token);
    /**
     * 移除过期TOKEN
     */
    int removeExpireToken();

}
