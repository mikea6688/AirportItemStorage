package org.code.airportitemstorage.service;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuartzService {

    private final Scheduler scheduler;

    public QuartzService(Scheduler scheduler) {
        this.scheduler = scheduler;
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
}
