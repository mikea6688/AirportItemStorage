package org.code.airportitemstorage.library.request.storage;

import lombok.Data;
import org.code.airportitemstorage.library.OperateStorageCabinetType;
import org.code.airportitemstorage.library.dto.order.UserOrderLogisticsDto;

@Data
public class OperateUserStorageCabinetRequest {
    public long orderId;

    public OperateStorageCabinetType operateType;

    public UserOrderLogisticsDto userOrderLogistics;
}
