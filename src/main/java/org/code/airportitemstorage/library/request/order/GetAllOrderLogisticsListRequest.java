package org.code.airportitemstorage.library.request.order;

import lombok.Data;

@Data
public class GetAllOrderLogisticsListRequest {
    public String storageUserAccount;

    public String cabinetNumber;

    public long pageIndex = 1;

    public long pageSize = 15;
}
