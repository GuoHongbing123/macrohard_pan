package com.vpsair.modules.pan.controller;

import com.vpsair.common.utils.Result;
import com.vpsair.modules.pan.dto.FileDTO;
import com.vpsair.modules.pan.dto.folder.FolderDTO;
import com.vpsair.modules.pan.entity.UserinfoEntity;
import com.vpsair.modules.pan.service.FileService;
import com.vpsair.modules.pan.service.FolderService;
import com.vpsair.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags="网盘目录")
@RestController
@RequestMapping("pan/directory")
public class DirectorController extends AbstractController {
    @Resource
    private FolderService folderService;
    @Resource
    private FileService fileService;

    @ApiOperation("目录-文件&文件夹")
    @GetMapping("list")
//    @RequiresPermissions("pan:api:folder")
    public Result<Map<String,Object>> list(@RequestParam(value = "folderId",required = false,defaultValue = "0")Long folderId){
         UserinfoEntity panUser = getPanUser();
         if(folderId.equals(0L)){
             folderId=Long.parseLong(panUser.getRemark());
         }
         List<FolderDTO> folderDTOList=folderService.getFoldersByUserIdAndFolderId(folderId,panUser.getId());
         List<FileDTO> fileDTOList=fileService.getFilesByUserIdAndFolderId(folderId,panUser.getId());
         Map<String,Object> map=new HashMap<>();
         map.put("folder",folderDTOList);
         map.put("file",fileDTOList);
         return new Result<Map<String, Object>>().ok(map);
    }

    @ApiOperation("目录-文件夹")
    @GetMapping("folder")
//    @RequiresPermissions("pan:api:folder")
    public Result<List<FolderDTO>> folder(@RequestParam(value = "folderId",required = false,defaultValue = "0")Long folderId){
        UserinfoEntity panUser = getPanUser();
        if(folderId.equals(0L)){
            folderId=Long.parseLong(panUser.getRemark());
        }
        List<FolderDTO> folderDTOList=folderService.getFoldersByUserIdAndFolderId(folderId,panUser.getId());
        return new Result<List<FolderDTO>>().ok(folderDTOList);
    }
}
