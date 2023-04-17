package com.vpsair.modules.pan.dao;

import com.vpsair.modules.pan.dto.FileDTO;
import com.vpsair.modules.pan.entity.FileEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 文件 DAO
 * @author Shen
 * @email syf0412@qq.com
 * @date 2021-10-30 21:42:21
 */
@Mapper
public interface FileDao extends BaseMapper<FileEntity> {

    int renameFile(Long id, String realName, Long panUserId);

    int moveFile(Long id, Long folderId, Long panUserId);

    List<FileDTO> findByShareFid(Long fid);

    List<FileDTO> getShareInfoByPid(String fid);

    int checkFileOnly(Long folderId, String realName);

    Long getTotalSize(Long userId);

}
