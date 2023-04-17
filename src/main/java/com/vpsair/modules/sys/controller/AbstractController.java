package com.vpsair.modules.sys.controller;

import com.vpsair.modules.pan.entity.UserinfoEntity;
import com.vpsair.modules.pan.service.FolderService;
import com.vpsair.modules.pan.service.UserinfoService;
import com.vpsair.modules.sys.entity.SysUserEntity;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * Controller公共组件
 */
public abstract class AbstractController {
	@Resource
	private UserinfoService userinfoService;
	@Resource
	private FolderService folderService;

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected SysUserEntity getUser() {
		return (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
	}

	protected Long getUserId() {
		return getUser().getUserId();
	}

	protected UserinfoEntity getPanUser() {
		return userinfoService.getBySysId(getUserId());
	}

	protected Boolean checkFolderOwner(Long folderId){
		return folderService.checkFolderOwner(getUserId(),folderId);
	}
}
