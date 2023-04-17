package com.vpsair.modules.pan.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vpsair.common.ConvertUtils;
import com.vpsair.modules.pan.Enum.FileStatusEnum;
import com.vpsair.modules.pan.dto.FileDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vpsair.common.utils.PageUtils;
import com.vpsair.common.utils.PageDTO;
import com.vpsair.common.utils.Query;

import com.vpsair.modules.pan.dao.FileDao;
import com.vpsair.modules.pan.entity.FileEntity;
import com.vpsair.modules.pan.service.FileService;

import javax.annotation.Resource;

/**
 * 文件 接口实现类
 * @author Shen
 * @email syf0412@qq.com
 * @date 2021-10-30 21:42:21
 */
@Service("fileService")
public class FileServiceImpl extends ServiceImpl<FileDao, FileEntity> implements FileService {

    @Resource
    private FileDao fileDao;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<FileEntity> page = this.page(
                new Query<FileEntity>().getPage(params),
                new QueryWrapper<FileEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageDTO<FileEntity> getPage(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<FileEntity> fileList = baseMapper.selectList(new QueryWrapper<FileEntity>().orderByDesc("id"));
        PageInfo<FileEntity> pageInfo = new PageInfo<>(fileList);
        return new PageDTO<>(page, limit, pageInfo.getTotal(), fileList);
    }

    @Override
    public void saveOrUpdateByKey(FileEntity fileEntity) {
        FileEntity file = baseMapper.selectOne(new QueryWrapper<FileEntity>().eq("file_key",fileEntity.getFileKey()));
        if (file == null){
            save(fileEntity);
        }else {
            fileEntity.setId(file.getId());
            updateById(fileEntity);
        }
    }

    @Override
    public List<FileDTO> getByKey(String key) {
        List<FileEntity> fileEntities = baseMapper.selectList(new QueryWrapper<FileEntity>().eq("file_key", key).apply("shard_index=shard_total"));
        return ConvertUtils.sourceToTarget(fileEntities,FileDTO.class);
    }

    @Override
    public FileDTO getUnfinishedByKey(String key) {
        FileEntity fileEntity = baseMapper.selectOne(new QueryWrapper<FileEntity>().eq("file_key", key).apply("shard_index!=shard_total"));
        return ConvertUtils.sourceToTarget(fileEntity, FileDTO.class);
    }

    @Override
    public List<FileDTO> getFilesByUserIdAndFolderId(Long folderId, Long userId) {
        QueryWrapper<FileEntity> wrapper=new QueryWrapper<>();
        wrapper.eq("folder_id",folderId).eq("owner_id",userId).eq("status", FileStatusEnum.OK.getCode()).orderByDesc("create_time");
        List<FileEntity> fileEntities=baseMapper.selectList(wrapper);
        return ConvertUtils.sourceToTarget(fileEntities,FileDTO.class);
    }

    @Override
    public int renameFile(Long id, String realName, Long panUserId) {
        return fileDao.renameFile(id,realName,panUserId);
    }

    @Override
    public int moveFile(Long id, Long folderId, Long panUserId) {
        int i=fileDao.moveFile(id,folderId,panUserId);
        // 更新原文件夹是否为空
        // 更新目标文件夹不为空
        return i;
    }

    @Override
    public List<FileDTO> findByShareFid(Long fid) {
        return fileDao.findByShareFid(fid);
    }

    @Override
    public List<FileDTO> getShareInfoByPid(String fid) {
        return fileDao.getShareInfoByPid(fid);
    }

    @Override
    public List<FileEntity> getByFolderId(Long folderId) {
        return baseMapper.selectList(new QueryWrapper<FileEntity>().eq("folder_id",folderId).eq("status",1));
    }

    @Override
    public List<FileEntity> getByFId(Long folderId) {
        return baseMapper.selectList(new QueryWrapper<FileEntity>().eq("folder_id",folderId).eq("status",2));
    }

    @Override
    public int checkFileOnly(Long folderId, String realName) {
        return fileDao.checkFileOnly(folderId,realName);
    }
}