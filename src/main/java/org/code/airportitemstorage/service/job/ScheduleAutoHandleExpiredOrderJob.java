package org.code.airportitemstorage.service.job;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

import org.code.airportitemstorage.library.entity.orders.OrderExpiredNotifyRecord;
import org.code.airportitemstorage.library.entity.storageCabinet.StorageCabinet;
import org.code.airportitemstorage.library.entity.user.User;
import org.code.airportitemstorage.mapper.storageCabinet.StorageCabinetMapper;
import org.code.airportitemstorage.mapper.users.UserMapper;
import org.code.airportitemstorage.service.order.OrderService;
import org.code.airportitemstorage.service.storageCabinet.StorageCabinetService;
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

    private final UserMapper userMapper;
    private final OrderMapper orderMapper;
    private final StorageCabinetService storageCabinetService;
    private final StorageCabinetMapper storageCabinetMapper;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("ScheduleAutoHandleExpiredOrderJob -- Job executed at: {}", LocalDateTime.now());

        var currentDate = LocalDateTime.now();

        List<Order> orders = orderMapper.selectList(
                new QueryWrapper<Order>().eq("storage_status", OrderStorageStatus.Using)
        );

        if (orders.isEmpty())return;

        var userIds = orders.stream().map(Order::getUserId).toList();
        List<User> users = userMapper.selectBatchIds(userIds);

        var cabinetIds = orders.stream().map(Order::getStorageCabinetId).toList();
        List<StorageCabinet> storageCabinets = storageCabinetMapper.selectBatchIds(cabinetIds);

        for (Order order : orders) {
            User user = users.stream().filter(x -> x.getId() == order.getUserId()).findFirst().orElse(null);
            StorageCabinet storageCabinet = storageCabinets.stream().filter(x -> x.getId() == order.getStorageCabinetId()).findFirst().orElse(null);

            var endDate = JobEngine.CalculateOrderEndDate(order);

            if(currentDate.isAfter(endDate) && ChronoUnit.DAYS.between(endDate, currentDate) > 3)
            {
                // 处理过期订单
                try {
                    storageCabinetService.HandleDiscardedOrder(order, storageCabinet, user, Duration.between(order.getStorageTime(), currentDate).toSeconds());
                    log.info("ScheduleAutoHandleExpiredOrderJob -- Job executed done. OrderId: {}", order.getId());
                }
                catch (Exception e) {
                    log.error("Fail to handle expired order.{}", e.getMessage());
                }
            }
        }
    }
}
