package com.vpsair.modules.pan.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vpsair.common.ConvertUtils;
import com.vpsair.modules.pan.dto.FileDTO;
import com.vpsair.modules.pan.dto.folder.FolderDTO;
import com.vpsair.modules.pan.dto.folder.WhereAmI;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vpsair.common.utils.PageUtils;
import com.vpsair.common.utils.PageDTO;
import com.vpsair.common.utils.Query;

import com.vpsair.modules.pan.dao.FolderDao;
import com.vpsair.modules.pan.entity.FolderEntity;
import com.vpsair.modules.pan.service.FolderService;

import javax.annotation.Resource;

/**
 * 文件夹 接口实现类
 * @author Shen
 * @email syf0412@qq.com
 * @date 2021-10-30 21:42:21
 */
@Service("folderService")
public class FolderServiceImpl extends ServiceImpl<FolderDao, FolderEntity> implements FolderService {

    @Resource
    private FolderDao folderDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<FolderEntity> page = this.page(
                new Query<FolderEntity>().getPage(params),
                new QueryWrapper<FolderEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageDTO<FolderEntity> getPage(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<FolderEntity> folderList = baseMapper.selectList(new QueryWrapper<FolderEntity>().orderByDesc("id"));
        PageInfo<FolderEntity> pageInfo = new PageInfo<>(folderList);
        return new PageDTO<>(page, limit, pageInfo.getTotal(), folderList);
    }

    @Override
    public Long createFolder(Long pid, String folderName, Long ownerId, String ownerName, Integer sort) {
        FolderEntity folder=new FolderEntity();
        folder.setPid(pid);
        folder.setRealName(folderName);
        folder.setFullPath("");
        folder.setOwnerId(ownerId);
        folder.setOwnerName(ownerName);
        folder.setShare(0);
        folder.setIsEmpty(0);
        folder.setStatus(1);
        save(folder);
        // TODO 更新文件夹是否为空
        return folder.getId();
    }

    @Override
    public List<FolderDTO> getFoldersByUserIdAndFolderId(Long folderId, Long userId) {
        QueryWrapper<FolderEntity> wrapper=new QueryWrapper<>();
        wrapper.eq("pid",folderId).eq("owner_id",userId).eq("status",1).orderByDesc("create_time");
        List<FolderEntity> folderEntities=baseMapper.selectList(wrapper);
        return ConvertUtils.sourceToTarget(folderEntities,FolderDTO.class);
    }

    @Override
    public Boolean checkFolderOwner(Long sysId,Long folderId) {
        return folderDao.checkFolderOwner(sysId,folderId)>0;
    }

    @Override
    public int renameFolder(Long fid, String realName, Long panUserId) {
        return folderDao.renameFolder(fid,realName,panUserId);
    }

    @Override
    public int moveFolder(Long fid, Long pid, Long panUserId) {
        int i=folderDao.moveFolder(fid,pid,panUserId);
        // 更新原文件夹是否为空
        // 更新目标文件夹不为空
        return i;
    }

    @Override
    public WhereAmI getPathByPid(Long fid) {
        return folderDao.getPathByPid(fid);
    }

    @Override
    public List<WhereAmI> getWhereAmI(List<WhereAmI> whereAmI) {
        Long pid=whereAmI.get(whereAmI.size()-1).getPid();
        if(pid==0){
            return whereAmI;
        }
        whereAmI.add(getPathByPid(pid));
        return getWhereAmI(whereAmI);
    }

    @Override
    public List<FolderDTO> findByShareFid(Long fid) {
        return folderDao.findByShareFid(fid);
    }

    @Override
    public List<FolderDTO> getShareInfoByPid(String fid) {
        return folderDao.getShareInfoByPid(fid);
    }

    @Override
    public List<FolderEntity> getByPid(Long folderId) {
        return baseMapper.selectList(new QueryWrapper<FolderEntity>().eq("pid",folderId).eq("status",1));
    }

    @Override
    public List<FolderEntity> getByFid(Long folderId) {
        return baseMapper.selectList(new QueryWrapper<FolderEntity>().eq("pid",folderId));
    }

    @Override
    public int checkFolderOnly(Long pid, String realName) {
        return folderDao.checkFolderOnly(pid,realName);
    }
}