package com.vpsair.modules.pan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vpsair.common.ConvertUtils;
import com.vpsair.common.exception.AirException;
import com.vpsair.common.utils.*;
import com.vpsair.modules.pan.dao.DownloadTokenDao;
import com.vpsair.modules.pan.dto.DownloadTokenDTO;
import com.vpsair.modules.pan.dto.GetDownloadDTO;
import com.vpsair.modules.pan.entity.DownloadTokenEntity;
import com.vpsair.modules.pan.entity.FileEntity;
import com.vpsair.modules.pan.entity.FolderEntity;
import com.vpsair.modules.pan.service.DownloadTokenService;
import com.vpsair.modules.pan.service.FileService;
import com.vpsair.modules.pan.service.FolderService;
import com.vpsair.modules.sys.oauth2.TokenGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

@Service("DownloadTokenService")
public class DownloadTokenServiceImpl extends ServiceImpl<DownloadTokenDao,DownloadTokenEntity> implements DownloadTokenService {

    @Resource
    private DownloadTokenDao downloadTokenDao;
    @Resource
    private FileService fileService;
    @Resource
    private FolderService folderService;
    @Value("${file.path}")
    private String FILE_PATH;

    @Override
    public PageUtils queryPage(Map<String, Object> params){
        IPage<DownloadTokenEntity> page= this.page(
                new Query<DownloadTokenEntity>().getPage(params),
                new QueryWrapper<DownloadTokenEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageDTO<DownloadTokenEntity> getPage(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List <DownloadTokenEntity> downloadTokenList = baseMapper.selectList(new QueryWrapper<DownloadTokenEntity>().orderByDesc("id"));
        PageInfo<DownloadTokenEntity> pageInfo = new PageInfo<>(downloadTokenList);
        return new PageDTO<>(page, limit, pageInfo.getTotal(), downloadTokenList);
    }

    @Override
    public GetDownloadDTO generateDLLink(Long id, Integer type) {
        GetDownloadDTO downloadDTO = new GetDownloadDTO();
        DownloadTokenEntity tokenFile = downloadTokenDao.getByFileKey(id.toString());
        if (tokenFile != null){
            setGetDownloadDTO(downloadDTO, tokenFile);
        }else if (type == 1){
            FileEntity file = fileService.getById(id);
            tokenFile = new DownloadTokenEntity();
            tokenFile.setToken(TokenGenerator.generateValue());
            tokenFile.setExpireDate(DateUtils.addDateHours(new Date(), 1));
            tokenFile.setFullPath(file.getRealPath());
            tokenFile.setRealName(file.getRealName());
            tokenFile.setFileKey(file.getId().toString());
            save(tokenFile);
            setGetDownloadDTO(downloadDTO, tokenFile);

        }else if (type == 2){
            FolderEntity folder = folderService.getById(id);
            tokenFile = doFolderZip(folder);
            setGetDownloadDTO(downloadDTO, tokenFile);
        }else {
            throw new AirException("目标文件未找到");
        }
        return downloadDTO;

    }

    private DownloadTokenEntity doFolderZip(FolderEntity folder) {
        ZipOutputStream out = null;
        String fullPath = FILE_PATH + File.separator + "zip" + File.separator + folder.getRealName() + ".zip";
        try {
            out = new ZipOutputStream(new FileOutputStream(fullPath));
            doZip(folder.getId(),out,"");
        } catch (IOException e) {
            e.printStackTrace();
            throw new AirException("目标文件未找到");
        }finally {
            try {
                if (out != null) out.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignore) {
        }
        DownloadTokenEntity downloadTokenEntity = new DownloadTokenEntity();
        downloadTokenEntity.setToken(TokenGenerator.generateValue());
        downloadTokenEntity.setRealName(folder.getRealName() + ".zip");
        downloadTokenEntity.setFileKey(folder.getId().toString());
        downloadTokenEntity.setFullPath(fullPath);
        downloadTokenEntity.setExpireDate(DateUtils.addDateHours(new Date(), 1));
        save(downloadTokenEntity);
        return downloadTokenEntity;

    }

    private void doZip(Long folderId, ZipOutputStream out, String dirName) throws IOException {
        List<FileEntity> files = fileService.getByFolderId(folderId);
        for (FileEntity file : files){
            PanZipUtils.doZip(new File(file.getRealPath()), out, dirName, file.getRealName());
        }
        List<FolderEntity> folders = folderService.getByPid(folderId);
        for (FolderEntity folder : folders){
            doZip(folder.getId(), out, folder.getRealName() );
        }
    }

    @Override
    public DownloadTokenDTO getByToken(Long id, String token) {
        DownloadTokenEntity dto = downloadTokenDao.getById(id,new Date());
        if (dto != null && dto.getToken().equals(token)){
            return ConvertUtils.sourceToTarget(dto, DownloadTokenDTO.class);

        }
        removeById(id);
        throw new AirException("token失效");
    }

    @Override
    public int removeExpireToken() {
        return downloadTokenDao.removeExpireToken(new Date());
    }

    private void setGetDownloadDTO(GetDownloadDTO downloadDTO, DownloadTokenEntity tokenFile) {
        downloadDTO.setId(tokenFile.getId());
        downloadDTO.setToken(tokenFile.getToken());
        downloadDTO.setExpireDate(tokenFile.getExpireDate());
        downloadDTO.setDlLink("api/pan/download/stream/"+downloadDTO.getId()+"?token="+downloadDTO.getToken());
    }

}

