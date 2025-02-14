package org.code.airportitemstorage.config;

import org.code.airportitemstorage.job.ScheduleAutoCheckOrderJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.UUID;


@Configuration
public class QuartzConfig {

    private final String groupName = "AirportItemStorage";

    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(ScheduleAutoCheckOrderJob.class)
                .withIdentity(ScheduleAutoCheckOrderJob.class.getName(), groupName)
                .usingJobData(UUID.randomUUID().toString(), true)
                .build();
    }

    @Bean
    public Trigger trigger() {
        return TriggerBuilder.newTrigger()
                .withIdentity("myTrigger", groupName)
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInMinutes(1) // 每5分钟执行一次
                        .repeatForever())
                .build();
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(JobDetail jobDetail, Trigger trigger) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setJobDetails(jobDetail);
        factory.setTriggers(trigger);
        return factory;
    }
}
