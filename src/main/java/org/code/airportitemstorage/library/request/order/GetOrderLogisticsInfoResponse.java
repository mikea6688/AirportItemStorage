package org.code.airportitemstorage.library.request.order;

import lombok.Data;
import org.code.airportitemstorage.library.dto.order.OrderLogisticsDto;

import java.util.List;

@Data
public class GetOrderLogisticsInfoResponse {
    public GetOrderLogisticsInfoResponse(List<OrderLogisticsDto> logisticsInfo, long total) {
        this.logisticsInfo = logisticsInfo;
        this.total = total;
    }

    public List<OrderLogisticsDto> logisticsInfo;

    public long total;
}
