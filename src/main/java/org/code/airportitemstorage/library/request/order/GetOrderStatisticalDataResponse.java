package org.code.airportitemstorage.library.request.order;

import lombok.Data;
import org.code.airportitemstorage.library.dto.order.OrderStatisticalDto;
import java.util.List;

@Data
public class GetOrderStatisticalDataResponse {
    public GetOrderStatisticalDataResponse(List<OrderStatisticalDto> data) {
        this.data = data;
    }

    public List<OrderStatisticalDto> data;
}
