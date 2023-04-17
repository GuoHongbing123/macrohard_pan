package com.vpsair.modules.pan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vpsair.common.utils.PageUtils;
import com.vpsair.common.utils.PageDTO;
import com.vpsair.modules.pan.dto.FileDTO;
import com.vpsair.modules.pan.dto.folder.FolderDTO;
import com.vpsair.modules.pan.dto.folder.WhereAmI;
import com.vpsair.modules.pan.entity.FolderEntity;

import java.util.List;
import java.util.Map;

/**
 * 文件夹 接口层
 * @author Shen
 * @email syf0412@qq.com
 * @date 2021-10-30 21:42:21
 */
public interface FolderService extends IService<FolderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取分页
     */
    PageDTO<FolderEntity> getPage(Integer page, Integer limit);
    /**
     * 创建初始文件夹
     */
    Long createFolder(Long pid,String folderName,Long ownerId,String ownerName,Integer sort);
    /**
     * 根据文件夹ID和用户ID查询
     */
    List<FolderDTO> getFoldersByUserIdAndFolderId(Long folderId, Long userId);
    /**
     * 校验文件夹所有者
     */
    Boolean checkFolderOwner(Long sysId,Long folderId);
    /**
     * 重命名文件夹
     */
    int renameFolder(Long fid, String realName, Long panUserId);
    /**
     * 移动文件夹
     */
    int moveFolder(Long fid, Long pid, Long panUserId);
    /**
     * 查询文件夹位置
     */
    WhereAmI getPathByPid(Long fid);

    List<WhereAmI> getWhereAmI(List<WhereAmI> whereAmI);

    List<FolderDTO> findByShareFid(Long fid);

    List<FolderDTO> getShareInfoByPid(String fid);
    /**
     * 获取文件夹下的文件夹
     */
    List<FolderEntity> getByPid(Long folderId);
    /**
     * 获取文件夹下已“删除”的文件夹
     */
    List<FolderEntity> getByFid(Long folderId);
    /**
     * 根据pid和文件名计数
     */
    int checkFolderOnly(Long pid, String realName);
}

