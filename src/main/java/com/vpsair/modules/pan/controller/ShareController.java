package com.vpsair.modules.pan.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.vpsair.common.exception.AirException;
import com.vpsair.modules.pan.dto.RequestShareDTO;
import com.vpsair.modules.pan.dto.SaveDTO;
import com.vpsair.modules.pan.dto.ShareDTO;
import com.vpsair.modules.pan.entity.FileEntity;
import com.vpsair.modules.pan.entity.UserinfoEntity;
import com.vpsair.modules.pan.service.FileService;
import com.vpsair.modules.pan.service.FolderService;
import com.vpsair.modules.pan.service.UserinfoService;
import com.vpsair.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;

import com.vpsair.modules.pan.entity.ShareEntity;
import com.vpsair.modules.pan.service.ShareService;
import com.vpsair.common.utils.PageDTO;
import com.vpsair.common.utils.Result;

import javax.annotation.Resource;

/**
 * 分享信息 控制层
 * @author Shen
 * @email syf0412@qq.com
 * @date 2021-10-30 23:06:48
 */
@Api(tags = "分享信息")
@RestController
@RequestMapping("pan/share")
public class ShareController extends AbstractController {
    @Autowired
    private ShareService shareService;
    @Resource
    private FileService fileService;
    @Resource
    private FolderService folderService;
    @Resource
    private UserinfoService userinfoService;

    /**
     * 列表
     */
    @ApiOperation("分享-列表")
    @GetMapping("/list")
    public Result<List<ShareDTO>> list(){
        List<ShareDTO> shareDTOList=shareService.getUserShareList(getPanUser());
        return new Result<List<ShareDTO>>().ok(shareDTOList);
    }
    /**
     * 转存
     */
    @ApiOperation("分享-转存")
    @PostMapping("/t_save")
    @Transactional
    public Result<Object> t_save(@RequestBody SaveDTO saveDTO){
        UserinfoEntity panUser = getPanUser();
        if(saveDTO.getTargetFolderId()==0){
            saveDTO.setTargetFolderId(Long.parseLong(panUser.getRemark()));
        }
        //校验目标文件夹是否为本人
        if(!checkFolderOwner(saveDTO.getTargetFolderId())){
            return new Result<>().error("目标文件夹未找到");
        }
        if(saveDTO.getFileId()!=null){
            FileEntity file = fileService.getById(saveDTO.getFileId());
            if(file==null){
                return new Result<>().error("目标文件夹未找到");
            }
            //校验用户网盘空间
            if(panUser.getTotalSize()+file.getSize()>=panUser.getSizeLimit()){
                throw new AirException("网盘可用空间不足");
            }
            file.setId(null);
            file.setFolderId(saveDTO.getTargetFolderId());
            file.setOwnerId(panUser.getId());
            file.setOwnerName(panUser.getUsername());
            file.setShare(0);
            file.setShareId(null);
            file.setRemark(null);
            file.setCreateTime(null);
            file.setUpdateTime(null);
            fileService.save(file);
            //更新网盘用户空间
            panUser.setTotalSize(panUser.getTotalSize()+file.getSize());
            userinfoService.updateById(panUser);
        }else {
            Long totalSize=shareService.saveFolder(saveDTO.getFolderId(),saveDTO.getTargetFolderId(),panUser,0L);
            //校验用户网盘空间
            if(panUser.getTotalSize()+totalSize>=panUser.getSizeLimit()){
                throw new AirException("网盘可用空间不足");
            }
            //更新网盘用户空间
            panUser.setTotalSize(panUser.getTotalSize()+totalSize);
            userinfoService.updateById(panUser);
        }
        return Result.success();
    }
    /**
     * 创建分享
     */
    @ApiOperation("分享-详情")
    @GetMapping("/detail/{link}")
    public Result<ShareDTO> detail(@PathVariable String link){
        ShareDTO shareDTO=shareService.getBySharePath(link);
        return new Result<ShareDTO>().ok(shareDTO);
    }
    /**
     * 鉴权
     */
    @ApiOperation("分享-鉴权")
    @GetMapping("/auth/{link}")
    public Result<Map<String,Object>> auth(@PathVariable String link,@RequestParam(value="code",required = false)String code){
        Map<String,Object> map=shareService.authShare(link,code);
        return new Result<Map<String,Object>>().ok(map);
    }
    /**
     * 目录列表
     */
    @ApiOperation("分享-目录列表")
    @GetMapping("/auth/{link}/folder")
    public Result<Map<String,Object>> folder(@PathVariable String link,
                                             @RequestParam(value="code",required = false)String code,
                                             @RequestParam(value = "fid",required = false)String fid){
        Map<String,Object> map=shareService.getShareFolderList(link,code,fid);
        return new Result<Map<String,Object>>().ok(map);
    }
    /**
     * 创建分享
     */
    @ApiOperation("分享-发起")
    @PostMapping("/create")
    public Result<ShareDTO> create(@RequestBody RequestShareDTO requestShareDTO){
        ShareDTO shareDTO=shareService.createShare(getPanUser(),requestShareDTO);
        return new Result<ShareDTO>().ok(shareDTO);
    }
//    /**
//     * 列表
//     */
//    @ApiOperation("分享信息-列表")
//    @GetMapping("/list")
//    @RequiresPermissions("pan:share:list")
//    public R list(@RequestParam Map<String, Object> params){
//        PageUtils page = shareService.queryPage(params);
//        return R.ok().put("page", page);
//    }

    /**
     * 分页
     */
    @ApiOperation("文件-分页")
    @GetMapping("/page")
    @RequiresPermissions("pan:file:list")
    public Result<PageDTO<ShareEntity>> page(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit){
        PageDTO<ShareEntity> pageDTO = shareService.getPage(page, limit);
        return new Result<PageDTO<ShareEntity>>().ok(pageDTO);
    }

    /**
     * 信息
     */
    @ApiOperation("分享信息-信息")
    @GetMapping("/info/{id}")
    @RequiresPermissions("pan:share:info")
    public Result<ShareEntity> info(@PathVariable("id") Long id){
		ShareEntity share = shareService.getById(id);
        return new Result<ShareEntity>().ok(share);
    }

    /**
     * 保存
     */
    @ApiOperation("分享信息-保存")
    @PostMapping("/save")
    @RequiresPermissions("pan:share:save")
    public Result<Object> save(@RequestBody ShareEntity share){
		shareService.save(share);
        return Result.success();
    }

    /**
     * 修改
     */
    @ApiOperation("分享信息-修改")
    @PostMapping("/update")
    @RequiresPermissions("pan:share:update")
    public Result<Object> update(@RequestBody ShareEntity share){
		shareService.updateById(share);
        return Result.success();
    }

    /**
     * 删除
     */
    @ApiOperation("分享信息-删除")
    @PostMapping("/delete")
    @RequiresPermissions("pan:share:delete")
    public Result<Object> delete(@RequestBody Long[] ids){
		shareService.removeByIds(Arrays.asList(ids));
        return Result.success();
    }

}
