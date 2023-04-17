package com.vpsair.modules.pan.dao;

import com.vpsair.modules.pan.dto.FileDTO;
import com.vpsair.modules.pan.dto.folder.FolderDTO;
import com.vpsair.modules.pan.dto.folder.WhereAmI;
import com.vpsair.modules.pan.entity.FolderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 文件夹 DAO
 * @author Shen
 * @email syf0412@qq.com
 * @date 2021-10-30 21:42:21
 */
@Mapper
public interface FolderDao extends BaseMapper<FolderEntity> {

    int checkFolderOwner(Long sysId,Long folderId);

    int renameFolder(Long fid, String realName, Long panUserId);

    int moveFolder(Long fid, Long pid, Long panUserId);

    WhereAmI getPathByPid(Long fid);

    List<FolderDTO> findByShareFid(Long fid);

    List<FolderDTO> getShareInfoByPid(String fid);

    int checkFolderOnly(Long pid, String realName);
}
