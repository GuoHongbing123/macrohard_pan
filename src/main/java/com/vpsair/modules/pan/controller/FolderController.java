package com.vpsair.modules.pan.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.vpsair.common.validator.ValidatorUtils;
import com.vpsair.modules.pan.dto.folder.RenameMoveFolderDTO;
import com.vpsair.modules.pan.dto.folder.WhereAmI;
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

import com.vpsair.modules.pan.entity.FolderEntity;
import com.vpsair.modules.pan.service.FolderService;
import com.vpsair.common.utils.PageUtils;
import com.vpsair.common.utils.PageDTO;
import com.vpsair.common.utils.Result;
import com.vpsair.common.utils.R;

/**
 * 文件夹 控制层
 * @author Shen
 * @email syf0412@qq.com
 * @date 2021-10-30 21:42:21
 */
@Api(tags = "文件夹")
@RestController
@RequestMapping("pan/folder")
public class FolderController extends AbstractController {
    @Autowired
    private FolderService folderService;

    /**
     * whereAmI
     */
    @ApiOperation("whereAmI")
    @GetMapping("whereAmI")
//    @RequiresPermissions("pan:api:folder")
    public Result<Object> whereAmI(@RequestParam("id") Long fid){
        ValidatorUtils.require(fid,"文件夹ID");
        if(fid==0){
            fid=Long.parseLong(getPanUser().getRemark());
        }
        if(!checkFolderOwner(fid)){
            return new Result<>().error("无权查看");
        }
        List<WhereAmI> whereAmI=new ArrayList<>();
        whereAmI.add(folderService.getPathByPid(fid));
        whereAmI=folderService.getWhereAmI(whereAmI);
        return new Result<>().ok(whereAmI);
    }
    /**
     * 重命名
     */
    @ApiOperation("文件夹-重命名")
    @PostMapping("rename")
//    @RequiresPermissions("pan:api:folder")
    public Result<Object> rename(@RequestBody RenameMoveFolderDTO dto){
        ValidatorUtils.require(dto.getRealName(),"文件夹名称");
        ValidatorUtils.require(dto.getId(),"文件夹ID");
        UserinfoEntity panUser = getPanUser();
        // TODO 检验父文件夹下是否存在同名文件夹
        if(folderService.checkFolderOnly(dto.getPid(),dto.getRealName())>=1){
            return new Result<>().error("已存在该文件夹");
        }
        int i=folderService.renameFolder(dto.getId(),dto.getRealName(),panUser.getId());
        return i>0?Result.success():new Result<>().error("文件夹权限错误");
    }
    /**
     * 移动
     */
    @ApiOperation("文件夹-移动")
    @PostMapping("move")
//    @RequiresPermissions("pan:api:folder")
    public Result<Object> move(@RequestBody RenameMoveFolderDTO dto){
        ValidatorUtils.require(dto.getId(),"文件夹ID");
        ValidatorUtils.require(dto.getPid(),"父文件夹");
        if(dto.getPid().equals(dto.getId())||!checkFolderOwner(dto.getPid())){
            return new Result<>().error("移动目标文件夹错误");
        }
        UserinfoEntity panUser = getPanUser();
        // TODO 检验父文件夹下是否存在同名文件夹
        if(folderService.checkFolderOnly(dto.getPid(),dto.getRealName())>=1){
            return new Result<>().error("已存在该文件夹");
        }
        int i=folderService.moveFolder(dto.getId(),dto.getPid(),panUser.getId());
        return i>0?Result.success():new Result<>().error("文件夹权限错误");
    }
    /**
     * 新建
     */
    @ApiOperation("文件夹-新建")
    @PostMapping("create")
//    @RequiresPermissions("pan:api:folder")
    public Result<Object> create(@RequestBody RenameMoveFolderDTO dto){
        ValidatorUtils.require(dto.getRealName(),"文件夹名称");
        ValidatorUtils.require(dto.getPid(),"父文件夹");
        UserinfoEntity panUser = getPanUser();
        // TODO 校验用户空间是否超过限制
        if(dto.getPid()==0){
            dto.setPid(Long.parseLong(panUser.getRemark()));
        }
        // 校验父文件夹下是否存在同名文件夹
        if(folderService.checkFolderOnly(dto.getPid(),dto.getRealName())>=1){
            return new Result<>().error("已存在该文件夹");
        }
        Long folderId = folderService.createFolder(dto.getPid(), dto.getRealName(), panUser.getId(),panUser.getUsername(),0);
        return folderId!=null?Result.success():new Result<>().error("文件夹创建失败");
    }
    /**
     * 列表
     */
    @ApiOperation("文件夹-列表")
    @GetMapping("/list")
    @RequiresPermissions("pan:folder:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = folderService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 分页
     */
    @ApiOperation("文件-分页")
    @GetMapping("/page")
    @RequiresPermissions("pan:file:list")
    public Result<PageDTO<FolderEntity>> page(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit){
        PageDTO<FolderEntity> pageDTO = folderService.getPage(page, limit);
        return new Result<PageDTO<FolderEntity>>().ok(pageDTO);
    }

    /**
     * 信息
     */
    @ApiOperation("文件夹-信息")
    @GetMapping("/info/{id}")
    @RequiresPermissions("pan:folder:info")
    public Result<FolderEntity> info(@PathVariable("id") Long id){
		FolderEntity folder = folderService.getById(id);
        return new Result<FolderEntity>().ok(folder);
    }

    /**
     * 保存
     */
    @ApiOperation("文件夹-保存")
    @PostMapping("/save")
    @RequiresPermissions("pan:folder:save")
    public Result<Object> save(@RequestBody FolderEntity folder){
		folderService.save(folder);
        return Result.success();
    }

    /**
     * 修改
     */
    @ApiOperation("文件夹-修改")
    @PostMapping("/update")
    @RequiresPermissions("pan:folder:update")
    public Result<Object> update(@RequestBody FolderEntity folder){
		folderService.updateById(folder);
        return Result.success();
    }

    /**
     * 删除
     */
    @ApiOperation("文件夹-删除")
    @PostMapping("/delete")
    @RequiresPermissions("pan:folder:delete")
    public Result<Object> delete(@RequestBody Long[] ids){
		folderService.removeByIds(Arrays.asList(ids));
        return Result.success();
    }

}
