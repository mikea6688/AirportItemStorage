package org.code.airportitemstorage.job.recurringJob;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class MyJob  implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("每10秒执行定时任务 - " + System.currentTimeMillis());
    }
}
