package com.vpsair.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.vpsair.common.utils.PageUtils;
import com.vpsair.modules.sys.entity.SysLogEntity;

import java.util.Map;


/**
 * 系统日志
 */
public interface SysLogService extends IService<SysLogEntity> {

    PageUtils queryPage(Map<String, Object> params);

}
