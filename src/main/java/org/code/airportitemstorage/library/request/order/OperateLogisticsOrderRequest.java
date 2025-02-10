package org.code.airportitemstorage.library.request.order;

import lombok.Data;
import org.code.airportitemstorage.library.OperateLogisticsOrderType;

@Data
public class OperateLogisticsOrderRequest {
    public long orderId;

    public OperateLogisticsOrderType operateType;
}
