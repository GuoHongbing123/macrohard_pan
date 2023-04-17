package com.vpsair.modules.pan.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vpsair.common.ConvertUtils;
import com.vpsair.common.exception.AirException;
import com.vpsair.modules.pan.dto.FileDTO;
import com.vpsair.modules.pan.dto.RequestShareDTO;
import com.vpsair.modules.pan.dto.ShareDTO;
import com.vpsair.modules.pan.dto.folder.FolderDTO;
import com.vpsair.modules.pan.entity.FileEntity;
import com.vpsair.modules.pan.entity.FolderEntity;
import com.vpsair.modules.pan.entity.UserinfoEntity;
import com.vpsair.modules.pan.service.FileService;
import com.vpsair.modules.pan.service.FolderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vpsair.common.utils.PageUtils;
import com.vpsair.common.utils.PageDTO;
import com.vpsair.common.utils.Query;

import com.vpsair.modules.pan.dao.ShareDao;
import com.vpsair.modules.pan.entity.ShareEntity;
import com.vpsair.modules.pan.service.ShareService;

import javax.annotation.Resource;

/**
 * 分享信息 接口实现类
 * @author Shen
 * @email syf0412@qq.com
 * @date 2021-10-30 23:06:48
 */
@Service("shareService")
public class ShareServiceImpl extends ServiceImpl<ShareDao, ShareEntity> implements ShareService {

    @Resource
    private FileService fileService;
    @Resource
    private FolderService folderService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ShareEntity> page = this.page(
                new Query<ShareEntity>().getPage(params),
                new QueryWrapper<ShareEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageDTO<ShareEntity> getPage(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<ShareEntity> shareList = baseMapper.selectList(new QueryWrapper<ShareEntity>().orderByDesc("id"));
        PageInfo<ShareEntity> pageInfo = new PageInfo<>(shareList);
        return new PageDTO<>(page, limit, pageInfo.getTotal(), shareList);
    }

    @Override
    public List<ShareDTO> getUserShareList(UserinfoEntity panUser) {
        // TODO 更新用户的过期分享、超出下载次数的分享
        List<ShareEntity> shareEntities = baseMapper.selectList(new QueryWrapper<ShareEntity>().eq("owner_id", panUser.getId()).
                eq("status", 1).orderByDesc("id"));
        return ConvertUtils.sourceToTarget(shareEntities,ShareDTO.class);
    }

    @Override
    public ShareDTO createShare(UserinfoEntity panUser, RequestShareDTO requestShareDTO) {
        // 校验文件权限
        // 生成分享信息
        ShareDTO shareDTO=new ShareDTO();
        BeanUtils.copyProperties(requestShareDTO,shareDTO);
        if(requestShareDTO.getType().equals(1)){// 1代表文件
            FileEntity file=fileService.getById(requestShareDTO.getFid());
            if(file==null||!file.getOwnerId().equals(panUser.getId())){
                throw new AirException("暂无权限");
            }
            shareDTO.setRealName(file.getRealName());
            //更新文件的分享状态
        }
        else if(requestShareDTO.getType().equals(2)){// 2代表文件夹
            FolderEntity folder=folderService.getById(requestShareDTO.getFid());
            if(folder==null||!folder.getOwnerId().equals(panUser.getId())){
                throw new AirException("暂无权限");
            }
            shareDTO.setRealName(folder.getRealName());
            //更新文件的分享状态
        }
        shareDTO.setOwnerId(panUser.getId());
        shareDTO.setOwnerName(panUser.getUsername());
        shareDTO.setSharePath(UUID.randomUUID().toString().replace("-",""));
        shareDTO.setStatus(1);
        shareDTO.setDownloadNum(0);
        shareDTO.setDownloadLimit(0);
        if(requestShareDTO.getShareType().equals("1")){
            shareDTO.setCode(generateShareCode());
        }
        save(ConvertUtils.sourceToTarget(shareDTO,ShareEntity.class));
        return shareDTO;
    }

    @Override
    public String generateShareCode() {
        String str="0,1,2,3,4,5,6,7,8,9,a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z";
        String[] str2=str.split(",");
        Random random=new Random();
        int index=0;
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<4;i++){
            index=random.nextInt(str2.length-1);
            sb.append(str2[index]);
        }
        return sb.toString();
    }

    @Override
    public ShareDTO getBySharePath(String link) {
        ShareEntity shareEntity = baseMapper.selectOne(new QueryWrapper<ShareEntity>().eq("share_path", link).eq("status", 1));
        if(shareEntity==null){
            throw new AirException("分享丢失啦！");
        }
        ShareDTO shareDTO=new ShareDTO();
        shareDTO.setOwnerName(shareEntity.getOwnerName());
        shareDTO.setRealName(shareEntity.getRealName());
        shareDTO.setShareType(shareEntity.getShareType());
        shareEntity.setType(shareEntity.getType());
        return shareDTO;
    }

    @Override
    public Map<String, Object> authShare(String link, String code) {
        ShareEntity shareEntity = baseMapper.selectOne(new QueryWrapper<ShareEntity>().eq("share_path", link).eq("status", 1));
        if(shareEntity==null){
            throw new AirException("分享丢失啦！");
        }else if (!shareEntity.getCode().equals(code)){
            throw new AirException("提取码错误");
        }
        Map<String,Object> map=new HashMap<>();
        if(shareEntity.getType().equals(1)){
            List<FileDTO> fileDTOS=fileService.findByShareFid(shareEntity.getFid());
            map.put("folder",null);
            map.put("file",fileDTOS);
        }else if (shareEntity.getType().equals(2)){
            List<FolderDTO> folderDTOS=folderService.findByShareFid(shareEntity.getFid());
            map.put("file",null);
            map.put("folder",folderDTOS);
        }else{
            throw new AirException("分享丢失啦");
        }
        return map;
    }

    @Override
    public Map<String, Object> getShareFolderList(String link, String code, String fid) {
        ShareEntity shareEntity = baseMapper.selectOne(new QueryWrapper<ShareEntity>().eq("share_path", link).eq("status", 1));
        if(shareEntity==null){
            throw new AirException("分享丢失啦！");
        }else if (!shareEntity.getCode().equals(code)){
            throw new AirException("提取码错误");
        }
        Map<String,Object> map=new HashMap<>();
        List<FileDTO> fileDTOS=fileService.getShareInfoByPid(fid);
        List<FolderDTO> folderDTOS=folderService.getShareInfoByPid(fid);
        map.put("file",fileDTOS);
        map.put("folder",folderDTOS);
        return map;
    }

    @Override
    public Long saveFolder(Long folderId, Long targetId, UserinfoEntity panUser, Long size) {
        //获取需要转存的文件夹信息
        FolderEntity targetFolder = folderService.getById(folderId);
        //复制这文件夹的信息
        targetFolder.setId(null);
        targetFolder.setPid(targetId);
        targetFolder.setShare(0);
        targetFolder.setOwnerId(panUser.getId());
        targetFolder.setOwnerName(panUser.getUsername());
        targetFolder.setCreateTime(null);
        targetFolder.setUpdateTime(null);
        folderService.save(targetFolder);
        //复制这文件夹下面的文件，累加size
        List<FileEntity> fileEntityList=fileService.getByFolderId(folderId);
        for (FileEntity file : fileEntityList) {
            file.setId(null);
            file.setFolderId(targetFolder.getId());
            file.setOwnerId(panUser.getId());
            file.setOwnerName(panUser.getUsername());
            file.setShare(0);
            file.setShareId(null);
            file.setRemark(null);
            file.setCreateTime(null);
            file.setUpdateTime(null);
            fileService.save(file);
            size+=file.getSize();
        }
        //循环遍历这个文件夹下面的文件夹
        List<FolderEntity> folderEntityList=folderService.getByPid(folderId);
        for (FolderEntity folder : folderEntityList) {
            saveFolder(folder.getId(),targetFolder.getId(),panUser,size);
        }
        return size;
    }

}