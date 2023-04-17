package com.vpsair.modules.pan.service.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vpsair.common.ConvertUtils;
import com.vpsair.common.exception.AirException;
import com.vpsair.common.utils.Result;
import com.vpsair.modules.pan.Enum.UserStatusEnum;
import com.vpsair.modules.pan.dao.FileDao;
import com.vpsair.modules.pan.dto.userinfo.*;
import com.vpsair.modules.pan.service.FileService;
import com.vpsair.modules.pan.service.FolderService;
import com.vpsair.modules.sys.entity.SysUserEntity;
import com.vpsair.modules.sys.service.SysUserService;
import com.vpsair.modules.sys.service.SysUserTokenService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vpsair.common.utils.PageUtils;
import com.vpsair.common.utils.PageDTO;
import com.vpsair.common.utils.Query;

import com.vpsair.modules.pan.dao.UserinfoDao;
import com.vpsair.modules.pan.entity.UserinfoEntity;
import com.vpsair.modules.pan.service.UserinfoService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;

/**
 * 网盘用户 接口实现类
 * @author Shen
 * @email syf0412@qq.com
 * @date 2021-10-30 23:15:10
 */
@Service("userinfoService")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserinfoServiceImpl extends ServiceImpl<UserinfoDao, UserinfoEntity> implements UserinfoService {

    @Resource
    private UserinfoDao userinfoDao;
    @Resource
    private SysUserTokenService sysUserTokenService;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private FileDao fileDao;
    @Resource
    private FolderService folderService;

    @Value("${vpsair.password.token}")
    private String token;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserinfoEntity> page = this.page(
                new Query<UserinfoEntity>().getPage(params),
                new QueryWrapper<UserinfoEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageDTO<UserinfoEntity> getPage(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<UserinfoEntity> userinfoList = baseMapper.selectList(new QueryWrapper<UserinfoEntity>().orderByDesc("id"));
        PageInfo<UserinfoEntity> pageInfo = new PageInfo<>(userinfoList);
        return new PageDTO<>(page, limit, pageInfo.getTotal(), userinfoList);
    }

    @Override
    public Result<Map<String,Object>> login(LoginDTO loginDTO) {
        UserinfoEntity userinfo = baseMapper.selectOne(new QueryWrapper<UserinfoEntity>().eq("username", loginDTO.getUsername()));
        if(null==userinfo||!userinfo.getPassword().equals(handlePassword(loginDTO.getPassword()))){
            return new Result<Map<String,Object>>().error("用户名或密码错误！");
        }
        else if(userinfo.getStatus().equals(UserStatusEnum.BLOCKED.getCode())){
            return new Result<Map<String,Object>>().error("用户被封禁！");
        }
        Map<String,Object> map=new HashMap<>();
        map.put("token",sysUserTokenService.getToken(userinfo.getSysId()));
        userinfo.setSysId(null);
        userinfo.setPassword(null);
        map.put("userinfo",ConvertUtils.sourceToTarget(userinfo, UserinfoDTO.class));
        return new Result<Map<String,Object>>().ok(map);
    }

    @Override
    public int update(Long sysId, UpdateDTO updateDTO) {
        return userinfoDao.update(sysId,updateDTO.getNickname(),updateDTO.getMobile(),updateDTO.getMail(),updateDTO.getAvatar(),updateDTO.getRemark());
    }

    @Override
    public int changePassword(Long userId, ChangePasswordDTO changePasswordDTO) {
        UserinfoEntity userinfo = new UserinfoEntity();
        changePasswordDTO.setPassword(handlePassword(changePasswordDTO.getPassword()));
        if(userinfoDao.checkPassword(changePasswordDTO.getPassword())==0){
            return 0;
        }
        userinfo.setPassword(handlePassword(changePasswordDTO.getNewPassword()));
        return userinfoDao.changePassword(userId,userinfo.getPassword());
    }

    @Override
    public int forgetPassword(ChangePasswordDTO changePasswordDTO) {
        UserinfoEntity userinfo = new UserinfoEntity();
        changePasswordDTO.setPassword(handlePassword(changePasswordDTO.getPassword()));
        if(userinfoDao.checkUser(changePasswordDTO.getUsername(),changePasswordDTO.getMail())==0){
            return 0;
        }
        userinfo.setPassword(handlePassword(changePasswordDTO.getNewPassword()));
        return userinfoDao.forgetPassword(userinfo.getPassword(),changePasswordDTO.getUsername(),changePasswordDTO.getMail());
    }

    @Override
    public UserinfoEntity getBySysId(Long sysId) {
        return baseMapper.selectOne(new QueryWrapper<UserinfoEntity>().eq("sys_id",sysId));
    }

    @Override
    public Long getTotalSize(Long userId) {
        return fileDao.getTotalSize(userId);
    }

    @Override
    @Transactional
    public Result<UserinfoDTO> register(RegisterDTO registerDTO) {
        SysUserEntity sysUserEntity = new SysUserEntity();
        sysUserEntity.setUsername(registerDTO.getUsername());
        sysUserEntity.setPassword(registerDTO.getPassword());
        sysUserEntity.setSalt("");
        sysUserEntity.setStatus(1);
        sysUserEntity.setEmail(registerDTO.getMail());
        sysUserEntity.setMobile(registerDTO.getMobile());
        sysUserEntity.setCreateUserId(1L);
        sysUserEntity.setCreateTime(new Date());

        List<Long> roleIdList = new ArrayList<>();
        roleIdList.add(1461626131009548290L);

        sysUserEntity.setRoleIdList(roleIdList);
//        sysUserService.saveUser(sysUserEntity);
        try{
            sysUserService.saveUser(sysUserEntity);
        }catch (DuplicateKeyException e){
            throw new AirException("用户名重复，注册失败！");
        }catch (Exception e){
            log.error((e.getMessage()));
            throw new AirException("注册失败！");
        }
        UserinfoEntity userinfo = new UserinfoEntity();
        BeanUtils.copyProperties(registerDTO, userinfo);
        userinfo.setPassword(handlePassword(registerDTO.getPassword()));
        userinfo.setSysId(sysUserEntity.getUserId());
        userinfo.setAvatar("");//头像
        userinfo.setSizeLimit(5368709120L);
        userinfo.setTotalSize(0L);
        userinfo.setStatus(UserStatusEnum.MEMBER.getCode());
        save(userinfo);
        Long folderId=folderService.createFolder(0L,"",userinfo.getId(),userinfo.getUsername(),0);
        userinfo.setRemark(folderId.toString());
        updateById(userinfo);
        return new Result<UserinfoDTO>().ok(ConvertUtils.sourceToTarget(userinfo, UserinfoDTO.class));
    }

    @Override
    public String handlePassword(String password) {
        return DigestUtils.md5DigestAsHex((password+token).getBytes());
    }
}