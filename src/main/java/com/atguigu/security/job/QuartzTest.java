package com.atguigu.security.job;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

public class QuartzTest {
    public static void main(String[] args) throws SchedulerException {

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();//调度器
        JobDetail jobDetail = JobBuilder.newJob(HelloJob.class)
                .usingJobData("detail","Simon").usingJobData("count",0)
                .withIdentity("job1", "group1").build();//任务实例
//        jobDetail的一些属性
        System.out.println(jobDetail.getKey().getName());//job1
        System.out.println(jobDetail.getKey().getGroup());//group1
        //触发器
        SimpleTrigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1")//触发器组与上面JobDetail的group没有关系
                .usingJobData("trigger","trigger")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().repeatForever().withIntervalInSeconds(2))//每2s执行
                .startNow().build();
        Date date = scheduler.scheduleJob(jobDetail, trigger);//关联
        System.out.println(date);
        scheduler.start();

    }
}
