package com.cheer.hole.task;

import com.cheer.hole.Redis.RedisLookedService;
import com.cheer.hole.service.LookedService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class LookedTask extends QuartzJobBean {

    @Autowired
    private RedisLookedService redisLookedService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //2天清除一次key
        redisLookedService.clearLookedFromRedis();
    }
}
