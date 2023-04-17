package com.vpsair.modules.pan.controller;

import java.util.Arrays;
import java.util.Map;

import com.vpsair.common.validator.ValidatorUtils;
import com.vpsair.modules.pan.dto.userinfo.*;
import com.vpsair.modules.sys.controller.AbstractController;
import com.vpsair.modules.sys.service.SysUserTokenService;
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

import com.vpsair.modules.pan.entity.UserinfoEntity;
import com.vpsair.modules.pan.service.UserinfoService;
import com.vpsair.common.utils.PageUtils;
import com.vpsair.common.utils.PageDTO;
import com.vpsair.common.utils.Result;
import com.vpsair.common.utils.R;

/**
 * 网盘用户 控制层
 * @author Shen
 * @email syf0412@qq.com
 * @date 2021-10-30 23:15:10
 */
@Api(tags = "网盘用户")
@RestController
@RequestMapping("pan/userinfo")
public class UserinfoController extends AbstractController {
    @Autowired
    private UserinfoService userinfoService;
    @Autowired
    private SysUserTokenService sysUserTokenService;
    /**
     * 登录
     */
    @ApiOperation("网盘用户-登录")
    @PostMapping("/login")
    public Result<Map<String,Object>> login(@RequestBody LoginDTO loginDTO) {
        ValidatorUtils.require(loginDTO.getUsername(),"用户名");
        ValidatorUtils.require(loginDTO.getPassword(),"密码");
        return userinfoService.login(loginDTO);
    }
    /**
     * 登出
     */
    @ApiOperation("网盘用户-登出")
    @PostMapping("/logout")
    public Result<Object> logout() {
        sysUserTokenService.logout(getUserId());
        return Result.success();
    }
    /**
     * 注册
     */
    @ApiOperation("网盘用户-注册")
    @PostMapping("/register")
    public Result<UserinfoDTO> register(@RequestBody RegisterDTO registerDTO) {
        return userinfoService.register(registerDTO);
    }
    /**
     * 修改密码
     */
    @ApiOperation("网盘用户-修改密码")
    @PostMapping("/changePassword")
    @RequiresPermissions("pan:api:userinfo")
    public Result<Object> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        int i=userinfoService.changePassword(getUserId(),changePasswordDTO);
        return i>0? Result.success():new Result<>().error("原密码不匹配");
    }
    /**
     * 忘记密码
     */
    @ApiOperation("网盘用户-忘记密码")
    @PostMapping("/forgetPassword")
    public Result<Object> forgetPassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        int i=userinfoService.forgetPassword(changePasswordDTO);
        return i>0? Result.success():new Result<>().error("未找到该用户或邮箱");
    }
    /**
     * 用户信息
     */
    @ApiOperation("网盘用户-用户信息")
    @GetMapping("/userInfo")
    @RequiresPermissions("pan:api:userinfo")
    public Result<UserinfoEntity> userInfo() {
        UserinfoEntity panUser = getPanUser();
        Long size=userinfoService.getTotalSize(panUser.getId());
        panUser.setTotalSize(size);
        userinfoService.updateById(panUser);
        panUser.setId(null);
        panUser.setSysId(null);
        panUser.setPassword(null);
        panUser.setRemark(null);
        return new Result<UserinfoEntity>().ok(panUser);
    }
    /**
     * 更新信息
     */
    @ApiOperation("网盘用户-更新信息")
    @PostMapping("/modify")
    @RequiresPermissions("pan:api:userinfo")
    public Result<Object> modify(@RequestBody UpdateDTO updateDTO) {
        int i=userinfoService.update(getUserId(),updateDTO);
        return i>0? Result.success():new Result<>().error();
    }

    /**
     * 列表
     */
    @ApiOperation("【管理】网盘用户-列表")
    @GetMapping("/list")
    @RequiresPermissions("pan:userinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = userinfoService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 分页
     */
    @ApiOperation("【管理】文件-分页")
    @GetMapping("/page")
    @RequiresPermissions("pan:file:list")
    public Result<PageDTO<UserinfoEntity>> page(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit){
        PageDTO<UserinfoEntity> pageDTO = userinfoService.getPage(page, limit);
        return new Result<PageDTO<UserinfoEntity>>().ok(pageDTO);
    }

    /**
     * 信息
     */
    @ApiOperation("【管理】网盘用户-信息")
    @GetMapping("/info/{id}")
    @RequiresPermissions("pan:userinfo:info")
    public Result<UserinfoEntity> info(@PathVariable("id") Long id){
		UserinfoEntity userinfo = userinfoService.getById(id);
        return new Result<UserinfoEntity>().ok(userinfo);
    }

    /**
     * 保存
     */
    @ApiOperation("【管理】网盘用户-保存")
    @PostMapping("/save")
    @RequiresPermissions("pan:userinfo:save")
    public Result<Object> save(@RequestBody UserinfoEntity userinfo){
		userinfoService.save(userinfo);
        return Result.success();
    }

    /**
     * 修改
     */
    @ApiOperation("【管理】网盘用户-修改")
    @PostMapping("/update")
    @RequiresPermissions("pan:userinfo:update")
    public Result<Object> update(@RequestBody UserinfoEntity userinfo){
		userinfoService.updateById(userinfo);
        return Result.success();
    }

    /**
     * 删除
     */
    @ApiOperation("【管理】网盘用户-删除")
    @PostMapping("/delete")
    @RequiresPermissions("pan:userinfo:delete")
    public Result<Object> delete(@RequestBody Long[] ids){
		userinfoService.removeByIds(Arrays.asList(ids));
        return Result.success();
    }

}
