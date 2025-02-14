package org.code.airportitemstorage.service;

import org.quartz.*;
import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.code.airportitemstorage.library.dto.job.QuartzJobConfig;
import org.code.airportitemstorage.service.job.ScheduleAutoCheckOrderForEmailJob;
import org.code.airportitemstorage.service.job.ScheduleAutoHandleExpiredOrderJob;

@Service
@Slf4j
public class QuartzService {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private QuartzJobConfig quartzJobConfig;

    @PostConstruct
    public void init() {
        try {
            for (QuartzJobConfig.JobConfig jobConfig : quartzJobConfig.getJobs()){
                JobDetail jobDetail = JobBuilder.newJob(getJobClass(jobConfig.getName()))
                        .withIdentity(jobConfig.getName(), jobConfig.getGroup())
                        .usingJobData(new JobDataMap(jobConfig.getJobData()))
                        .build();

                Trigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity(jobConfig.getName() + "-Trigger", jobConfig.getGroup())
                        .startNow()
                        .withSchedule(CronScheduleBuilder.cronSchedule(jobConfig.getCron()))
                        .build();

                log.info("EmailJob已被调度，开始循环执行");

                scheduler.scheduleJob(jobDetail, trigger);
            }
        }catch (SchedulerException e){
            log.error(e.getMessage());
        }
    }

    public void scheduleJob(JobDetail jobDetail, Trigger trigger){
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pauseJob(String jobName, String jobGroup){
        try {
            JobKey jobKey = new JobKey(jobName, jobGroup);
            scheduler.pauseJob(jobKey);
            System.out.println("暂停任务成功：" + jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void resumeJob(String jobName, String jobGroup){
        try {
            JobKey jobKey = new JobKey(jobName, jobGroup);
            scheduler.resumeJob(jobKey);
            System.out.println("恢复任务成功：" + jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void deleteJob(String jobName, String jobGroup){
        try {
            JobKey jobKey = new JobKey(jobName, jobGroup);
            boolean deleted = scheduler.deleteJob(jobKey);
            if (deleted) {
                System.out.println("删除任务成功：" + jobKey);
            } else {
                System.out.println("删除任务失败：" + jobKey);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    private Class<? extends Job> getJobClass(String jobName) {
        return switch (jobName) {
            case "ScheduleAutoCheckOrderForEmailJob" -> ScheduleAutoCheckOrderForEmailJob.class;
            case "ScheduleAutoHandleExpiredOrderJob" -> ScheduleAutoHandleExpiredOrderJob.class;
            default -> throw new IllegalArgumentException("无法找到任务类: " + jobName);
        };
    }
}
