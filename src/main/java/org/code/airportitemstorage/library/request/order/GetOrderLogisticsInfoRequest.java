package org.code.airportitemstorage.library.request.order;

import lombok.Data;

@Data
public class GetOrderLogisticsInfoRequest {
    public long pageIndex = 1;

    public long pageSize = 15;
}
