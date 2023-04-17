package com.vpsair.modules.pan.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.vpsair.modules.pan.dto.*;
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

import com.vpsair.modules.pan.entity.RecycleBinEntity;
import com.vpsair.modules.pan.service.RecycleBinService;
import com.vpsair.common.utils.PageUtils;
import com.vpsair.common.utils.PageDTO;
import com.vpsair.common.utils.Result;
import com.vpsair.common.utils.R;

/**
 * 回收站 控制层
 * @author Shen
 * @email syf0412@qq.com
 * @date 2021-10-30 23:06:48
 */
@Api(tags = "回收站")
@RestController
@RequestMapping("pan/recyclebin")
public class RecycleBinController extends AbstractController {
    @Autowired
    private RecycleBinService recycleBinService;

    /**
     * 回收站列表
     */
    @ApiOperation("回收站-列表（目录）")
    @GetMapping("/RecycleBinList")
    public Result<List<RecycleBinDTO>> RecycleBinList(){
        List<RecycleBinDTO> recycleBinDTOList=recycleBinService.getRecycleBinList(getPanUser());
        return new Result<List<RecycleBinDTO>>().ok(recycleBinDTOList);
    }
    /**
     * 移动到回收站
     */
    @ApiOperation("回收站-移动到回收站")
    @PostMapping("/remove")
    public Result<Object> remove(@RequestBody RequestRecycleBinDTO requestRecycleBinDTO){
        RecycleBinDTO recycle=recycleBinService.remove(getPanUser(),requestRecycleBinDTO);
        if (null != recycle) {
            return new Result<>().ok("移动到回收站成功");
        }
        return new Result<>().error("移动到回收站失败");
    }
    /**
     * 还原
     */
    @ApiOperation("回收站-还原")
    @PostMapping("/restore")
    public Result<RecycleBinDTO> restore(@RequestBody RestoreOrDeleteDTO restoreOrDeleteDTO){
        RecycleBinDTO recycle=recycleBinService.restore(getPanUser(),restoreOrDeleteDTO);
        return new Result<RecycleBinDTO>().ok("还原成功");
    }
    /**
     * 永久删除
     */
    @ApiOperation("回收站-永久删除")
    @PostMapping("/deleteForever")
    public Result<RecycleBinDTO> deleteForever(@RequestBody RestoreOrDeleteDTO restoreOrDeleteDTO){
        RecycleBinDTO recycle=recycleBinService.deleteForever(getPanUser(),restoreOrDeleteDTO);
        return new Result<RecycleBinDTO>().ok("删除成功");
    }




    /**
     * 列表
     */
    @ApiOperation("回收站-列表")
    @GetMapping("/list")
    @RequiresPermissions("pan:recyclebin:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = recycleBinService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 分页
     */
    @ApiOperation("文件-分页")
    @GetMapping("/page")
    @RequiresPermissions("pan:file:list")
    public Result<PageDTO<RecycleBinEntity>> page(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit){
        PageDTO<RecycleBinEntity> pageDTO = recycleBinService.getPage(page, limit);
        return new Result<PageDTO<RecycleBinEntity>>().ok(pageDTO);
    }

    /**
     * 信息
     */
    @ApiOperation("回收站-信息")
    @GetMapping("/info/{id}")
    @RequiresPermissions("pan:recyclebin:info")
    public Result<RecycleBinEntity> info(@PathVariable("id") Long id){
		RecycleBinEntity recycleBin = recycleBinService.getById(id);
        return new Result<RecycleBinEntity>().ok(recycleBin);
    }

    /**
     * 保存
     */
    @ApiOperation("回收站-保存")
    @PostMapping("/save")
    @RequiresPermissions("pan:recyclebin:save")
    public Result<Object> save(@RequestBody RecycleBinEntity recycleBin){
		recycleBinService.save(recycleBin);
        return Result.success();
    }

    /**
     * 修改
     */
    @ApiOperation("回收站-修改")
    @PostMapping("/update")
    @RequiresPermissions("pan:recyclebin:update")
    public Result<Object> update(@RequestBody RecycleBinEntity recycleBin){
		recycleBinService.updateById(recycleBin);
        return Result.success();
    }

    /**
     * 删除
     */
    @ApiOperation("回收站-删除")
    @PostMapping("/delete")
    @RequiresPermissions("pan:recyclebin:delete")
    public Result<Object> delete(@RequestBody Long[] ids){
		recycleBinService.removeByIds(Arrays.asList(ids));
        return Result.success();
    }

}
