package org.code.airportitemstorage.service.job;

import org.code.airportitemstorage.library.entity.orders.Order;

import java.time.LocalDateTime;

public class JobEngine {

    public static LocalDateTime CalculateOrderEndDate(Order order) {
        if (order == null)return null;

        var endDate = order.getStorageTime();

        if(order.isUseMemberRenewalService()) endDate.plusDays(3);

        return switch (order.getDateType()){
            case ThreeDays -> endDate.plusDays(3);
            case OneWeek -> endDate.plusWeeks(1);
            case OneMonth -> endDate.plusMonths(1);
        };
    }
}
