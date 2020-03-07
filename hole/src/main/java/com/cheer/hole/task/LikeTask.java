package com.cheer.hole.task;

import com.cheer.hole.service.LikedService;
import com.cheer.hole.service.LookedService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.Date;

//这是点赞数据的定时刷回数据库的任务
@Slf4j
public class LikeTask extends QuartzJobBean {
    @Autowired
    private LikedService likedService;
    @Autowired
    private LookedService lookedService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("LikeTask-------- {}", sdf.format(new Date()));
        //将redis中的点赞数据存到数据库中并删除
        likedService.transLikedCountFromRedis2DB();
        likedService.transLikedFromRedis2DB();
        //因为浏览次数是最好频繁更新，就这么做了
        lookedService.transLookedCountFromRedis2DB();
    }
}
