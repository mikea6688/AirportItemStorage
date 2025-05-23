package org.code.airportitemstorage.controller;

import org.code.airportitemstorage.service.job.MyJob;
import org.code.airportitemstorage.service.QuartzService;
import org.code.airportitemstorage.service.email.IEmailService;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.quartz.*;

import java.util.Date;

@RestController
public class TestController {
    @Autowired
    private QuartzService quartzService;
    @Autowired
    private IEmailService emailService;

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
