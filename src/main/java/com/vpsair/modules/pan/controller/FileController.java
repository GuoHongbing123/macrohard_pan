package com.vpsair.modules.pan.controller;

import java.util.Arrays;
import java.util.Map;

import com.vpsair.common.validator.ValidatorUtils;
import com.vpsair.modules.pan.dto.folder.RenameMoveFileDTO;
import com.vpsair.modules.pan.dto.folder.RenameMoveFolderDTO;
import com.vpsair.modules.pan.entity.UserinfoEntity;
import com.vpsair.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;

import com.vpsair.modules.pan.entity.FileEntity;
import com.vpsair.modules.pan.service.FileService;
import com.vpsair.common.utils.PageUtils;
import com.vpsair.common.utils.PageDTO;
import com.vpsair.common.utils.Result;
import com.vpsair.common.utils.R;

/**
 * 文件 控制层
 * @author Shen
 * @email syf0412@qq.com
 * @date 2021-10-30 21:42:21
 */
@Api(tags = "文件")
@RestController
@RequestMapping("pan/file")
public class FileController extends AbstractController {
    @Autowired
    private FileService fileService;

    /**
     * 重命名
     */
    @ApiOperation("文件-重命名")
    @PostMapping("rename")
//    @RequiresPermissions("pan:api:file")
    public Result<Object> rename(@RequestBody RenameMoveFileDTO dto){
        ValidatorUtils.require(dto.getRealName(),"文件名称");
        ValidatorUtils.require(dto.getId(),"文件ID");
        UserinfoEntity panUser = getPanUser();
        // 检验父文件夹下是否存在同名文件夹
        if(fileService.checkFileOnly(dto.getFolderId(),dto.getRealName())>=1){
            return new Result<>().error("已存在该文件");
        }
        int i=fileService.renameFile(dto.getId(),dto.getRealName(),panUser.getId());
        return i>0?Result.success():new Result<>().error("文件权限错误");
    }
    /**
     * 移动
     */
    @ApiOperation("文件-移动")
    @PostMapping("move")
//    @RequiresPermissions("pan:api:file")
    public Result<Object> move(@RequestBody RenameMoveFileDTO dto){
        ValidatorUtils.require(dto.getId(),"文件ID");
        ValidatorUtils.require(dto.getFolderId(),"文件夹");
        if(!checkFolderOwner(dto.getFolderId())){
            return new Result<>().error("移动到目标文件夹错误");
        }
        UserinfoEntity panUser = getPanUser();
        // 检验父文件夹下是否存在同名文件夹
        if(fileService.checkFileOnly(dto.getFolderId(),dto.getRealName())>=1){
            return new Result<>().error("已存在该文件");
        }
        int i=fileService.moveFile(dto.getId(),dto.getFolderId(),panUser.getId());
        return i>0?Result.success():new Result<>().error("文件权限错误");
    }
    /**
     * 列表
     */
    @ApiOperation("文件-列表")
    @GetMapping("/list")
    @RequiresPermissions("pan:file:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = fileService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 分页
     */
    @ApiOperation("文件-分页")
    @GetMapping("/page")
    @RequiresPermissions("pan:file:list")
    public Result<PageDTO<FileEntity>> page(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit){
        PageDTO<FileEntity> pageDTO = fileService.getPage(page, limit);
        return new Result<PageDTO<FileEntity>>().ok(pageDTO);
    }

    /**
     * 信息
     */
    @ApiOperation("文件-信息")
    @GetMapping("/info/{id}")
    @RequiresPermissions("pan:file:info")
    public Result<FileEntity> info(@PathVariable("id") Long id){
		FileEntity file = fileService.getById(id);
        return new Result<FileEntity>().ok(file);
    }

    /**
     * 保存
     */
    @ApiOperation("文件-保存")
    @PostMapping("/save")
    @RequiresPermissions("pan:file:save")
    public Result<Object> save(@RequestBody FileEntity file){
		fileService.save(file);
        return Result.success();
    }

    /**
     * 修改
     */
    @ApiOperation("文件-修改")
    @PostMapping("/update")
    @RequiresPermissions("pan:file:update")
    public Result<Object> update(@RequestBody FileEntity file){
		fileService.updateById(file);
        return Result.success();
    }

    /**
     * 删除
     */
    @ApiOperation("文件-删除")
    @PostMapping("/delete")
    @RequiresPermissions("pan:file:delete")
    public Result<Object> delete(@RequestBody Long[] ids){
		fileService.removeByIds(Arrays.asList(ids));
        return Result.success();
    }

}
