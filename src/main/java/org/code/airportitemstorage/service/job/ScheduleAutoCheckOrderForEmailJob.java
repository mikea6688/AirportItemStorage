package org.code.airportitemstorage.service.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.code.airportitemstorage.library.OrderStorageStatus;
import org.code.airportitemstorage.library.entity.orders.Order;
import org.code.airportitemstorage.library.entity.orders.OrderExpiredNotifyRecord;
import org.code.airportitemstorage.library.entity.storageCabinet.StorageCabinet;
import org.code.airportitemstorage.library.entity.user.User;
import org.code.airportitemstorage.mapper.order.OrderExpiredNotifyRecordMapper;
import org.code.airportitemstorage.mapper.storageCabinet.StorageCabinetMapper;
import org.code.airportitemstorage.mapper.users.UserMapper;
import org.code.airportitemstorage.service.email.IEmailService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.code.airportitemstorage.mapper.order.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleAutoCheckOrderForEmailJob implements Job {

    private final UserMapper userMapper;
    private final OrderMapper orderMapper;
    private final IEmailService emailService;
    private final StorageCabinetMapper storageCabinetMapper;
    private final OrderExpiredNotifyRecordMapper orderExpiredNotifyRecordMapper;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        var queryOrderWrapper = new QueryWrapper<Order>();
        queryOrderWrapper.eq("storage_status", OrderStorageStatus.Using);

        List<Order> orders = orderMapper.selectList(queryOrderWrapper);

        if(orders.isEmpty())return;

        List<Long> orderIds = orders.stream().map(Order::getId).toList();
        List<Long> userIds = orders.stream().map(Order::getUserId).toList();
        List<Long> cabinetIds = orders.stream().map(Order::getStorageCabinetId).toList();

        List<StorageCabinet> storageCabinets = storageCabinetMapper.selectBatchIds(cabinetIds);

        List<User> users = userMapper.selectBatchIds(userIds);

        List<OrderExpiredNotifyRecord> expiredNotifyRecords = orderExpiredNotifyRecordMapper
                .selectList(new QueryWrapper<OrderExpiredNotifyRecord>().in("order_id", orderIds));

        var currentDate = LocalDateTime.now();

        for (Order order : orders) {
            OrderExpiredNotifyRecord expiredNotifyRecord = expiredNotifyRecords.stream()
                    .filter(x -> x.getOrderId() == order.getId())
                    .max(Comparator.comparing(OrderExpiredNotifyRecord::getCreatedDate)) // 按创建时间找到最近的
                    .orElse(null);

            StorageCabinet storageCabinet = storageCabinets.stream().filter(x -> x.getId() == order.getStorageCabinetId()).findFirst().orElse(null);

            User user = users.stream().filter(x -> x.getId() == order.getUserId()).findFirst().orElse(null);

            if(user == null || storageCabinet == null) continue;

            LocalDateTime endDate = JobEngine.CalculateOrderEndDate(order);

            if(Duration.between(currentDate, endDate).toDays() <= 3 &&
                    (expiredNotifyRecord == null ||
                            (Duration.between(expiredNotifyRecord.getCreatedDate(), endDate).toDays() <= 3)))
            {
                // 发邮件通知用户续费
                try {
                    emailService.sendEmail(user.getEmail(), "续费提醒！", "您的存储柜" + storageCabinet.getNum() + "还有三天到期，如想继续存储请及时续期。");

                    OrderExpiredNotifyRecord record = new OrderExpiredNotifyRecord();
                    record.setOrderId(order.getId());
                    record.setCreatedDate(currentDate);

                    orderExpiredNotifyRecordMapper.insert(record);
                }
                catch (Exception e) {
                    log.error("Fail to send email.{}", e.getMessage());
                }
            }
        }
    }
}