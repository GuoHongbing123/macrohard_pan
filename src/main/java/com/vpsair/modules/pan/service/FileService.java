package com.vpsair.modules.pan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vpsair.common.utils.PageUtils;
import com.vpsair.common.utils.PageDTO;
import com.vpsair.modules.pan.dto.FileDTO;
import com.vpsair.modules.pan.entity.FileEntity;

import java.util.List;
import java.util.Map;

/**
 * 文件 接口层
 * @author Shen
 * @email syf0412@qq.com
 * @date 2021-10-30 21:42:21
 */
public interface FileService extends IService<FileEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取分页
     */
    PageDTO<FileEntity> getPage(Integer page, Integer limit);

    void saveOrUpdateByKey(FileEntity fileEntity);

    List<FileDTO> getByKey(String key);

    /**
     * 获取未上传成功的文件信息
     */
    FileDTO getUnfinishedByKey(String key);

    /**
     * 根据folderId查询
     */
    List<FileDTO> getFilesByUserIdAndFolderId(Long folderId, Long userId);
    /**
     * 重命名文件
     */
    int renameFile(Long id, String realName, Long panUserId);
    /**
     * 移动文件
     */
    int moveFile(Long id, Long folderId, Long panUserId);
    /**
     * 根据fid查询
     */
    List<FileDTO> findByShareFid(Long fid);

    List<FileDTO> getShareInfoByPid(String fid);
    /**
     * 根据folderId查询
     */
    List<FileEntity> getByFolderId(Long folderId);
    /**
     * 根据folderId查询已“删除”的文件
     */
    List<FileEntity> getByFId(Long folderId);
    /**
     * 根据folderId和文件名计数
     */
    int checkFileOnly(Long folderId, String realName);

}

