package org.code.airportitemstorage.service.job;

import java.util.List;
import org.quartz.Job;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;
import org.code.airportitemstorage.mapper.order.OrderMapper;
import org.code.airportitemstorage.library.OrderStorageStatus;
import org.code.airportitemstorage.library.entity.orders.Order;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleAutoHandleExpiredOrderJob implements Job {

    private final OrderMapper orderMapper;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("ScheduleAutoHandleExpiredOrderJob -- Job executed at: {}", LocalDateTime.now());
        var queryOrderWrapper = new QueryWrapper<Order>();
        queryOrderWrapper.eq("storage_status", OrderStorageStatus.Using);

        List<Order> orders = orderMapper.selectList(queryOrderWrapper);

    }
}
