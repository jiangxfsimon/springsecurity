package com.atguigu.security.job;

import org.quartz.*;

import java.text.SimpleDateFormat;
import java.util.Date;


@PersistJobDataAfterExecution//多次调用Job会对job进行持久化，既保存数据的信息
public class HelloJob implements Job {

    public HelloJob() {//任务每次执行都会创建一个新的对象
        System.out.println("构造函数");
    }

    @Override//context获取运行时参数
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(context.getJobDetail().getJobDataMap().get("detail"));//JobDetail传递的参数
        System.out.println(context.getTrigger().getJobDataMap().get("trigger"));//Trigger传递的参数
        System.out.println("当前时间：" + format.format(context.getFireTime()) + " 下次任务时间："+format.format(context.getNextFireTime()));
        System.out.println(Thread.currentThread().getName());

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        Integer count = (Integer) jobDataMap.get("count");
        jobDataMap.put("count", ++count);
        System.out.println(count);
    }
}
