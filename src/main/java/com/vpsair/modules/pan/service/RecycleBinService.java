package com.vpsair.modules.pan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vpsair.common.utils.PageUtils;
import com.vpsair.common.utils.PageDTO;
import com.vpsair.modules.pan.dto.RecycleBinDTO;
import com.vpsair.modules.pan.dto.RequestRecycleBinDTO;
import com.vpsair.modules.pan.dto.RestoreOrDeleteDTO;
import com.vpsair.modules.pan.entity.RecycleBinEntity;
import com.vpsair.modules.pan.entity.UserinfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 回收站 接口层
 * @author Shen
 * @email syf0412@qq.com
 * @date 2021-10-30 23:06:48
 */
public interface RecycleBinService extends IService<RecycleBinEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取分页
     */
    PageDTO<RecycleBinEntity> getPage(Integer page, Integer limit);
    /**
     * 回收站列表
     */
    List<RecycleBinDTO> getRecycleBinList(UserinfoEntity panUser);
    /**
     * 移动到回收站
     */
    RecycleBinDTO remove(UserinfoEntity panUser, RequestRecycleBinDTO requestRecycleBinDTO);
    /**
     * 还原删除的文件和文件夹
     */
    RecycleBinDTO restore(UserinfoEntity panUser, RestoreOrDeleteDTO restoreOrDeleteDTO);
    /**
     * 永久删除
     */
    RecycleBinDTO deleteForever(UserinfoEntity panUser, RestoreOrDeleteDTO restoreOrDeleteDTO);
    /**
     * 递归“删除”或还原文件和文件夹
     */
    Long removeFolder(Long folderId,Long size,String flag);
    /**
     * 递归删除
     */
    void deleteFolder(Long folderId);

}

