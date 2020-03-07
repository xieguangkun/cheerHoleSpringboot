package com.cheer.hole.config;

import com.cheer.hole.task.LikeTask;
import com.cheer.hole.task.LookedTask;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//定时任务配置类
@Configuration
public class QuartzConfig {
    private static final String LIKE_TASK_IDENTITY = "LikeTaskQuartz";

    private static final String LOOKED_TASK_IDENTITY = "LookedTaskQuartz";

    @Bean(name = "twoHoursJobDetail")
    public JobDetail twoHoursDetail(){
        return JobBuilder.newJob(LikeTask.class).withIdentity(LIKE_TASK_IDENTITY).storeDurably().build();
    }

    @Bean(name="twoDaysJobDetail")
    public JobDetail twoDaysDetail(){
        return JobBuilder.newJob(LookedTask.class).withIdentity(LOOKED_TASK_IDENTITY).storeDurably().build();
    }

    @Bean(name = "twoHoursTrigger")
    public Trigger twoHoursTrigger(){
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
//                .withIntervalInSeconds(15)  //设置时间周期单位秒
//                .withIntervalInHours(2)  //两个小时执行一次
                .withIntervalInMinutes(5)//每五分钟刷一次，可以后续做排行榜
                .repeatForever();
        return TriggerBuilder.newTrigger().forJob(twoHoursDetail())
                .withIdentity(LIKE_TASK_IDENTITY)
                .withSchedule(scheduleBuilder)
                .build();
    }

    @Bean(name="twoDaysTrigger")
    public Trigger twoDaysTrigger(){
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
//                .withIntervalInSeconds(15)  //设置时间周期单位秒
                .withIntervalInHours(48)  //两天执行一次
                .repeatForever();
        return TriggerBuilder.newTrigger().forJob(twoDaysDetail())
                .withIdentity(LOOKED_TASK_IDENTITY)
                .withSchedule(scheduleBuilder)
                .build();
    }

}
