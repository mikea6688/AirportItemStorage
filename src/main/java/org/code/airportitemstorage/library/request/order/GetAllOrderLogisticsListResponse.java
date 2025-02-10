package org.code.airportitemstorage.library.request.order;

import org.code.airportitemstorage.library.dto.order.OrderLogisticsDto;

import java.util.List;

public class GetAllOrderLogisticsListResponse {
    public GetAllOrderLogisticsListResponse() {
    }

    public GetAllOrderLogisticsListResponse(List<OrderLogisticsDto> logisticsInfo, long total) {
        this.logisticsInfo = logisticsInfo;
        this.total = total;
    }

    public List<OrderLogisticsDto> logisticsInfo;

    public long total;
}
