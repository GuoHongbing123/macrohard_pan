package com.vpsair.modules.pan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vpsair.common.utils.PageUtils;
import com.vpsair.common.utils.PageDTO;
import com.vpsair.modules.pan.dto.RequestShareDTO;
import com.vpsair.modules.pan.dto.ShareDTO;
import com.vpsair.modules.pan.entity.ShareEntity;
import com.vpsair.modules.pan.entity.UserinfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 分享信息 接口层
 * @author Shen
 * @email syf0412@qq.com
 * @date 2021-10-30 23:06:48
 */
public interface ShareService extends IService<ShareEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取分页
     */
    PageDTO<ShareEntity> getPage(Integer page, Integer limit);
    /**
     * 查询用户分享信息
     */
    List<ShareDTO> getUserShareList(UserinfoEntity panUser);
    /**
     * 创建分享
     */
    ShareDTO createShare(UserinfoEntity panUser, RequestShareDTO requestShareDTO);
    /**
     * 创建分享
     */
    String generateShareCode();
    /**
     * 根据path获取分享信息
     */
    ShareDTO getBySharePath(String link);
    /**
     * 鉴权
     */
    Map<String, Object> authShare(String link, String code);

    Map<String, Object> getShareFolderList(String link, String code, String fid);
    /**
     * 递归转存文件和文件夹
     */
    Long saveFolder(Long folderId, Long targetId, UserinfoEntity panUser, Long size);
}

