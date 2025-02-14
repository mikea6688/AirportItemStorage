package org.code.airportitemstorage.library.dto.job;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "custom-quartz")
public class QuartzJobConfig {

    private List<JobConfig> jobs;

    @Data
    public static class JobConfig {
        private String name;
        private String group;
        private String cron;
        private Map<String, Object> jobData;
    }
}
