package org.code.airportitemstorage.library.request.order;

import lombok.Data;
import org.code.airportitemstorage.library.OrderStorageStatus;

@Data
public class GetUserOrderRequest {
    public String num;

    public OrderStorageStatus status;

    public long pageIndex = 1;

    public long pageSize = 15;
}
