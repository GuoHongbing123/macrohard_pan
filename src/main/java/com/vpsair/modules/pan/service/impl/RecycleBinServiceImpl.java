package com.vpsair.modules.pan.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vpsair.common.ConvertUtils;
import com.vpsair.common.exception.AirException;
import com.vpsair.common.utils.DateUtils;
import com.vpsair.modules.pan.Enum.FileStatusEnum;
import com.vpsair.modules.pan.dao.FolderDao;
import com.vpsair.modules.pan.dto.*;
import com.vpsair.modules.pan.entity.*;
import com.vpsair.modules.pan.service.FileService;
import com.vpsair.modules.pan.service.FolderService;
import com.vpsair.modules.pan.service.UserinfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vpsair.common.utils.PageUtils;
import com.vpsair.common.utils.PageDTO;
import com.vpsair.common.utils.Query;

import com.vpsair.modules.pan.dao.RecycleBinDao;
import com.vpsair.modules.pan.service.RecycleBinService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 回收站 接口实现类
 * @author Shen
 * @email syf0412@qq.com
 * @date 2021-10-30 23:06:48
 */
@Service("recycleBinService")
public class RecycleBinServiceImpl extends ServiceImpl<RecycleBinDao, RecycleBinEntity> implements RecycleBinService {

    @Resource
    private FileService fileService;
    @Resource
    private FolderService folderService;
    @Resource
    private FolderDao folderDao;
    @Resource
    private UserinfoService userinfoService;
    @Resource
    private RecycleBinService recycleBinService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<RecycleBinEntity> page = this.page(
                new Query<RecycleBinEntity>().getPage(params),
                new QueryWrapper<RecycleBinEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageDTO<RecycleBinEntity> getPage(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<RecycleBinEntity> recycleBinList = baseMapper.selectList(new QueryWrapper<RecycleBinEntity>().orderByDesc("id"));
        PageInfo<RecycleBinEntity> pageInfo = new PageInfo<>(recycleBinList);
        return new PageDTO<>(page, limit, pageInfo.getTotal(), recycleBinList);
    }

    @Override
    public List<RecycleBinDTO> getRecycleBinList(UserinfoEntity panUser) {
        List<RecycleBinEntity> recycleBinEntities = baseMapper.selectList(new QueryWrapper<RecycleBinEntity>().eq("owner_id", panUser.getId()).
                eq("status", 1).orderByDesc("id"));
        return ConvertUtils.sourceToTarget(recycleBinEntities, RecycleBinDTO.class);
    }

    @Override
    public RecycleBinDTO remove(UserinfoEntity panUser, RequestRecycleBinDTO requestRecycleBinDTO) {
        RecycleBinDTO recycleBinDTO=new RecycleBinDTO();
        BeanUtils.copyProperties(requestRecycleBinDTO,recycleBinDTO);
        if(requestRecycleBinDTO.getType().equals(1)){// 1代表文件
            FileEntity file=fileService.getById(requestRecycleBinDTO.getFid());
            if(file==null||!file.getOwnerId().equals(panUser.getId())){
                throw new AirException("目标文件暂无权限");
            }
            recycleBinDTO.setRealName(file.getRealName());
            recycleBinDTO.setSize(file.getSize());
            recycleBinDTO.setRemark(file.getSuffix());
            // 更新文件状态
            file.setStatus(FileStatusEnum.HIDE.getCode());
            fileService.updateById(file);
            // 更新用户空间
            panUser.setTotalSize(panUser.getTotalSize()-file.getSize());
            userinfoService.updateById(panUser);
        }
        else if(requestRecycleBinDTO.getType().equals(2)){// 2代表文件夹
            FolderEntity folder=folderService.getById(requestRecycleBinDTO.getFid());
            if(folder==null||!folder.getOwnerId().equals(panUser.getId())){
                throw new AirException("目标文件夹暂无权限");
            }
            recycleBinDTO.setRealName(folder.getRealName());
            recycleBinDTO.setRemark("");
            // 更新文件夹状态
            folder.setStatus(2);
            folderService.updateById(folder);
            // 递归“删除”文件夹
            Long totalSize=removeFolder(folder.getId(),0L,"remove");
            recycleBinDTO.setSize(totalSize);
            panUser.setTotalSize(panUser.getTotalSize()-totalSize);
            userinfoService.updateById(panUser);
        }
        recycleBinDTO.setFullPath("");
        recycleBinDTO.setOwnerId(panUser.getId());
        recycleBinDTO.setOwnerName(panUser.getUsername());
        recycleBinDTO.setExpireDate(DateUtils.addDateDays(new Date(), 7));
        recycleBinDTO.setStatus(1);
        recycleBinDTO.setCreateTime(null);
        recycleBinDTO.setUpdateTime(null);

        save(ConvertUtils.sourceToTarget(recycleBinDTO,RecycleBinEntity.class));
        return recycleBinDTO;
    }

    @Override
    @Transactional // 事务，如果有SQL执行失败，将会对之前执行成功的SQL回滚
    public RecycleBinDTO restore(UserinfoEntity panUser, RestoreOrDeleteDTO restoreOrDeleteDTO) {
        RecycleBinDTO recycleBinDTO=new RecycleBinDTO();
        RecycleBinEntity recycleBinEntity=recycleBinService.getById(restoreOrDeleteDTO.getId());
        if(recycleBinEntity==null||!recycleBinEntity.getOwnerId().equals(panUser.getId())){
            throw new AirException("暂无权限");
        }
        if(restoreOrDeleteDTO.getType().equals(1)){// 1代表文件
            FileEntity file=fileService.getById(restoreOrDeleteDTO.getFid());
            if(file==null||!file.getOwnerId().equals(panUser.getId())){
                throw new AirException("目标文件暂无权限");
            }
            // 更新文件状态
            file.setStatus(FileStatusEnum.OK.getCode());
            fileService.updateById(file);
            // 更新用户空间
            panUser.setTotalSize(panUser.getTotalSize()+file.getSize());
            userinfoService.updateById(panUser);
        }
        else if(restoreOrDeleteDTO.getType().equals(2)){// 2代表文件夹
            FolderEntity folder=folderService.getById(restoreOrDeleteDTO.getFid());
            if(folder==null||!folder.getOwnerId().equals(panUser.getId())){
                throw new AirException("目标文件夹暂无权限");
            }
            // 递归还原文件夹
            Long totalSize=removeFolder(folder.getId(),0L,"restore");
            if(panUser.getTotalSize()+totalSize>=panUser.getSizeLimit()){
                throw new AirException("网盘可用空间不足,无法还原");
            }
            // 更新文件夹状态
            folder.setStatus(1);
            folderService.updateById(folder);
            recycleBinDTO.setSize(totalSize);
            panUser.setTotalSize(panUser.getTotalSize()+totalSize);
            userinfoService.updateById(panUser);
        }
        removeById(restoreOrDeleteDTO.getId());
        return recycleBinDTO;
    }

    @Override
    @Transactional
    public RecycleBinDTO deleteForever(UserinfoEntity panUser, RestoreOrDeleteDTO restoreOrDeleteDTO) {
        RecycleBinDTO recycleBinDTO=new RecycleBinDTO();
        RecycleBinEntity recycleBinEntity=recycleBinService.getById(restoreOrDeleteDTO.getId());
        if(recycleBinEntity==null||!recycleBinEntity.getOwnerId().equals(panUser.getId())){
            throw new AirException("暂无权限");
        }
        if(restoreOrDeleteDTO.getType().equals(1)){// 1代表文件
            FileEntity file=fileService.getById(restoreOrDeleteDTO.getFid());
            if(file==null||!file.getOwnerId().equals(panUser.getId())){
                throw new AirException("目标文件暂无权限");
            }
            // 更新文件状态
            file.setStatus(FileStatusEnum.BLOCK.getCode());
            fileService.updateById(file);
        }
        else if(restoreOrDeleteDTO.getType().equals(2)){// 2代表文件夹
            FolderEntity folder=folderService.getById(restoreOrDeleteDTO.getFid());
            if(folder==null||!folder.getOwnerId().equals(panUser.getId())){
                throw new AirException("目标文件夹暂无权限");
            }
            // 递归删除文件夹
            deleteFolder(folder.getId());
            folderDao.deleteById(folder.getId());
        }
        removeById(restoreOrDeleteDTO.getId());
        return recycleBinDTO;
    }

    @Override
    public Long removeFolder(Long folderId,Long size,String flag){
        if(flag.equals("remove")){
            //获取这文件夹下面的文件，累加size，修改状态
            List<FileEntity> fileEntityList=fileService.getByFolderId(folderId);
            for (FileEntity file : fileEntityList) {
                file.setStatus(FileStatusEnum.HIDE.getCode());
                size+=file.getSize();
                fileService.updateById(file);
            }
            //循环遍历这个文件夹下面的文件夹，修改状态
            List<FolderEntity> folderEntityList=folderService.getByFid(folderId);
            for (FolderEntity folder : folderEntityList) {
                folder.setStatus(2);
                folderService.updateById(folder);
                removeFolder(folder.getId(),size,flag);
            }
        }else if(flag.equals("restore")){
            //获取这文件夹下面的文件，累加size，修改状态
            List<FileEntity> fileEntityList=fileService.getByFId(folderId);
            for (FileEntity file : fileEntityList) {
                file.setStatus(FileStatusEnum.OK.getCode());
                size+=file.getSize();
                fileService.updateById(file);
            }
            //循环遍历这个文件夹下面的文件夹，修改状态
            List<FolderEntity> folderEntityList=folderService.getByFid(folderId);
            for (FolderEntity folder : folderEntityList) {
                folder.setStatus(1);
                folderService.updateById(folder);
                removeFolder(folder.getId(),size,flag);
            }
        }

        return size;
    }

    @Override
    public void deleteFolder(Long folderId) {
        //获取这文件夹下面的文件，删除
        List<FileEntity> fileEntityList=fileService.getByFId(folderId);
        for (FileEntity file : fileEntityList) {
            file.setStatus(FileStatusEnum.BLOCK.getCode());
            fileService.updateById(file);
        }
        //循环遍历这个文件夹下面的文件夹，删除
        List<FolderEntity> folderEntityList=folderService.getByFid(folderId);
        for (FolderEntity folder : folderEntityList) {
            folderDao.deleteById(folder.getId());
            deleteFolder(folder.getId());
        }
    }

}