package com.vpsair.modules.job.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vpsair.common.utils.PageUtils;
import com.vpsair.modules.job.entity.ScheduleJobLogEntity;

import java.util.Map;

/**
 * 定时任务日志
 */
public interface ScheduleJobLogService extends IService<ScheduleJobLogEntity> {

	PageUtils queryPage(Map<String, Object> params);
	
}
