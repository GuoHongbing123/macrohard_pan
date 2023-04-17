package com.vpsair.modules.pan.controller;

import com.vpsair.common.ConvertUtils;
import com.vpsair.common.exception.AirException;
import com.vpsair.common.utils.Base64ToMultipartFile;
import com.vpsair.common.utils.Result;
import com.vpsair.modules.pan.Enum.FileStatusEnum;
import com.vpsair.modules.pan.dto.FileDTO;
import com.vpsair.modules.pan.dto.UploadDTO;
import com.vpsair.modules.pan.entity.FileEntity;
import com.vpsair.modules.pan.entity.UserinfoEntity;
import com.vpsair.modules.pan.Enum.FileStatusEnum;
import com.vpsair.modules.pan.service.FileService;
import com.vpsair.modules.pan.service.FolderService;
import com.vpsair.modules.pan.service.UserinfoService;
import com.vpsair.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * 上传
 * @author Shen && syf0412@vip.qq.com
 */
@Api(tags = "文件上传")
@Slf4j
@RestController
@RequestMapping("pan/upload")
public class UploadController extends AbstractController {

    @Value("${file.path}")
    private String FILE_PATH;
    @Resource
    private FileService fileService;
    @Resource
    private FolderService folderService;
    @Resource
    private UserinfoService userinfoService;

    @ApiOperation(("极速秒传"))
    @PostMapping("check")
    public Result<Object>check(@RequestBody UploadDTO uploadDTO){
        UserinfoEntity panUser = getPanUser();
        // 校验用户网盘空间是否足够
        if(uploadDTO.getFolderId()==null||uploadDTO.getFolderId().equals(0L)){
            uploadDTO.setFolderId(Long.parseLong(panUser.getRemark()));
        }
        List<FileDTO> files = fileService.getByKey(uploadDTO.getKey());
        if(files.size()<1){
            // 根据key去查询是否有未上传成功的文件
            FileDTO fileDTO = fileService.getUnfinishedByKey(uploadDTO.getKey());
            if (null != fileDTO) {
                fileDTO.setRealName(null);
                fileDTO.setRealPath(null);
                return new Result<>().ok(202, fileDTO, "上传未完成");
            }
            return new Result<>().error(404,"服务器无此文件");
        }
        FileDTO fileDTO=files.get(0);
        if(panUser.getTotalSize()+fileDTO.getSize()>=panUser.getSizeLimit()){
            throw new AirException("网盘可用空间不足");
        }
        // 检验父文件夹下是否存在同名文件夹
        if(fileService.checkFileOnly(uploadDTO.getFolderId(),fileDTO.getRealName())>=1){
            return new Result<>().error("已存在该文件");
        }
        fileDTO.setId(null);
        fileDTO.setCreateTime(null);
        fileDTO.setUpdateTime(null);
        fileDTO.setOwnerId(panUser.getId());
        fileDTO.setOwnerName(panUser.getUsername());
        fileDTO.setStatus(FileStatusEnum.OK.getCode());
        fileDTO.setRealName(uploadDTO.getName());
        fileDTO.setFolderId(uploadDTO.getFolderId());
        fileService.save(ConvertUtils.sourceToTarget(fileDTO,FileEntity.class));
        // 更新用户网盘空间
        panUser.setTotalSize(panUser.getTotalSize()+fileDTO.getSize());
        userinfoService.updateById(panUser);
        return new Result<>().ok("极速秒传成功");
    }

    @ApiOperation("分片上传")
    @PostMapping("upload")
    public Result<Object> upload(@RequestBody UploadDTO uploadDTO) throws Exception {
        UserinfoEntity panUser = getPanUser();
        // TODO 用户网盘空间是否足够
        if(panUser.getTotalSize()+uploadDTO.getSize()>=panUser.getSizeLimit()){
            throw new AirException("网盘可用空间不足");
        }
        if(uploadDTO.getFolderId()==null||uploadDTO.getFolderId().equals(0L)){
            uploadDTO.setFolderId(Long.parseLong(panUser.getRemark()));
        }
        String key = uploadDTO.getKey();
        String suffix = uploadDTO.getSuffix();
        String shardBase64 = uploadDTO.getShard();
        // 将接收到的 base64 分片转换成 MultipartFile 类型
        MultipartFile shard = Base64ToMultipartFile.base64ToMultipart(shardBase64);

        String dir = "merge";
        File fullDir = new File(FILE_PATH + File.separator + dir);
        if (!fullDir.exists()) { // 如果文件夹不存在，则创建该文件夹
            fullDir.mkdir();
        }
//        String path = fullDir + File.separator + key + "." + suffix;
        String localPath = new StringBuffer(dir).append(File.separator).append(key).append(".").append(suffix).toString();
        String fullPath = FILE_PATH + File.separator + localPath;
        File dest = new File(fullPath + "." + uploadDTO.getShardIndex());
        shard.transferTo(dest);

        // 文件信息写入数据库
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFolderId(uploadDTO.getFolderId());
        fileEntity.setFullPath("");
        fileEntity.setRealName(uploadDTO.getName());
        fileEntity.setRealPath(fullPath);
        fileEntity.setSize(uploadDTO.getSize());
        fileEntity.setShardSize(uploadDTO.getShardSize());
        fileEntity.setSuffix(uploadDTO.getSuffix());
        fileEntity.setSort("0");
        fileEntity.setShardIndex(uploadDTO.getShardIndex());
        fileEntity.setShardTotal(uploadDTO.getShardTotal());
        fileEntity.setThumb("");
        fileEntity.setFileKey(key);
        fileEntity.setOwnerId(panUser.getId());
        fileEntity.setOwnerName(panUser.getUsername());
        fileEntity.setOwnerType("");
        fileEntity.setShare(0);
        fileEntity.setShareId(null);
        fileEntity.setStatus(FileStatusEnum.HIDE.getCode());

        // 校验分片上传完毕后合并文件
        if (uploadDTO.getShardTotal().equals(uploadDTO.getShardIndex())) {
            // 合并文件操作
            merge(fileEntity);
            // 更新用户的网盘空间
            panUser.setTotalSize(panUser.getTotalSize()+uploadDTO.getSize());
            userinfoService.updateById(panUser);
        }
        fileService.saveOrUpdateByKey(fileEntity);

        return Result.success();
    }

    private void merge(FileEntity fileEntity) throws IOException, InterruptedException {
        String path = FILE_PATH + File.separator + "merge";
        Integer shardTotal = fileEntity.getShardTotal();
        String date = LocalDate.now().toString().replace("-", "");
        String dir = FILE_PATH + File.separator + date;
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        String realPath = dir + File.separator + fileEntity.getFileKey() + "." + fileEntity.getSuffix();
        FileOutputStream fileOutputStream = new FileOutputStream(realPath, true);
        FileInputStream fileInputStream = null;
        byte[] byt = new byte[10 * 1024 * 1024];
        int len;
        try {
            for (int i = 0; i < shardTotal; i++) {
                String shardPath = new StringBuffer(path).
                        append(File.separator)
                        .append(fileEntity.getFileKey())
                        .append(".").append(fileEntity.getSuffix())
                        .append(".").append(i + 1).toString();
                fileInputStream = new FileInputStream(shardPath);
                while ((len = fileInputStream.read(byt)) != -1) {
                    fileOutputStream.write(byt, 0, len);
                }
            }
        }catch (IOException e) {
            log.error("分片合并异常, {}", e.getMessage());
        }finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                fileOutputStream.close();
                log.info("文件合并成功，IO流关闭");
            }catch (Exception e) {
                log.error("IO流关闭异常, {}", e.getMessage());
            }
        }
        System.gc();
        Thread.sleep(100);
        for (int i = 0; i < shardTotal; i++) {
            String shardPath = new StringBuffer(path).
                    append(File.separator)
                    .append(fileEntity.getFileKey())
                    .append(".").append(fileEntity.getSuffix())
                    .append(".").append(i + 1).toString();
            File file = new File(shardPath);
            file.delete();
        }
        fileEntity.setRealPath(realPath);
        fileEntity.setStatus(FileStatusEnum.OK.getCode());
        log.info("分片合并结束");
    }

}
