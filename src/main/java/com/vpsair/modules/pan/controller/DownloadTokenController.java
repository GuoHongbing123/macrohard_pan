package com.vpsair.modules.pan.controller;


import com.vpsair.common.exception.AirException;
import com.vpsair.common.utils.Result;
import com.vpsair.modules.pan.dto.DownloadTokenDTO;
import com.vpsair.modules.pan.dto.GetDownloadDTO;
import com.vpsair.modules.pan.service.DownloadTokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@Slf4j
@Api(tags = "文件下载")
@RestController
@RequestMapping("pan/download")

public class DownloadTokenController {
    @Autowired
    private DownloadTokenService downloadTokenService;


    /**
     * 列表
     */
    @ApiOperation("获取下载路径")
    @GetMapping("getPath")
    //@RequiresPermissions("pan:api:file")
    public Result<Object> getPath(@RequestParam("id") Long id,
                                  @RequestParam("type") Integer type){
        try {
            downloadTokenService.removeExpireToken();
        }catch (Exception ignore){}
        GetDownloadDTO getDownloadDTO = downloadTokenService.generateDLLink(id,type);
        if (getDownloadDTO == null){
            return new Result<>().error("目标文件未找到");
        }
        return new Result<>().ok(getDownloadDTO);
    }

    @ApiOperation("下载")
    @RequestMapping("stream/{id}")
    //@RequiresPermissions("pan:api:file")
    public void getPath(@PathVariable("id") Long id,
                        @RequestParam("token") String token,
                        HttpServletResponse response){
        DownloadTokenDTO downloadTokenDTO = downloadTokenService.getByToken(id, token);
        OutputStream out = null;
        Boolean flag = false; // 设置标记，判断是否为打包文件
        if (null != downloadTokenDTO && downloadTokenDTO.getFullPath().contains("E:/workspace/upload_files/zip/")){
            flag = true;
        }
        try {
            out=response.getOutputStream();
            response.setCharacterEncoding("UTF-8");
            byte[] stream = getStream(downloadTokenDTO.getFullPath());
            response.setHeader("Content-Disposition","atttachement;filename=" + URLEncoder.encode(downloadTokenDTO.getRealName(),"UTF-8"));
            response.setContentType("application/octet-stream;charset=UTF-8");
            IOUtils.write(stream,out);
            if (flag) { // 移除zip 压缩包
                File file = new File(downloadTokenDTO.getFullPath());
                if (!file.delete()){
                    file.deleteOnExit();
                }
            }
        }catch (Exception e){
            log.error("【下载异常！】{}",e.getMessage());
            throw new AirException("下载异常");
        }finally {
            try {
                if (out == null){
                    out.close();
                }
            }catch (IOException e){
                log.error(e.getMessage());
            }
            if (flag) { // 移除zip token
                downloadTokenService.removeById(downloadTokenDTO.getId());
            }
        }
    }

    private byte[] getStream(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte [] b= new byte[1024];
        int index;
        while ((index = fis.read(b)) != -1){
            bos.write(b,0,index);
        }
        fis.close();
        bos.close();
        return bos.toByteArray();
    }

}
