package org.code.airportitemstorage.controller;

import lombok.RequiredArgsConstructor;
import org.code.airportitemstorage.job.MyJob;
import org.code.airportitemstorage.service.QuartzService;
import org.code.airportitemstorage.service.email.IEmailService;
import org.quartz.JobDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.quartz.*;

import java.util.Date;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final QuartzService quartzService;
    private final IEmailService emailService;

    @GetMapping("job")
    public String job() {
        try {
            JobDetail jobDetail = JobBuilder.newJob(MyJob.class)
                    .storeDurably()
                    .withIdentity("myJob", "group1")
                    .usingJobData("name", "myJob")
                    .build();

            Date triggerTime = DateBuilder.futureDate(2, DateBuilder.IntervalUnit.MINUTE);

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("myTrigger", "group1")
                    .startAt(triggerTime)
//                    .startNow()  // 从当前时间开始执行
//                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
//                            .withIntervalInSeconds(10)  // 每10秒执行一次
//                            .repeatForever())
                    .build();

            quartzService.scheduleJob(jobDetail, trigger);
            return "任务已调度成功!";
        }
        catch (Exception e){
            e.printStackTrace();
            return "调度任务失败!";
        }
    }

    @PostMapping("sent/email")
    public String sentEmail(){
        try {
            emailService.sendEmail("ljxccboy1304055@gmail.com", "test", "test");
            return "邮件发送成功!";
        }
        catch (Exception e){
            e.printStackTrace();
            return "邮件发送失败!";
        }
    }
}
