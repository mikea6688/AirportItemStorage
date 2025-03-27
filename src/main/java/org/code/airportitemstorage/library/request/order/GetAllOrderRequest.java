package org.code.airportitemstorage.library.request.order;

import lombok.Data;

@Data
public class GetAllOrderRequest {
    private String cabinetNumber;

    private String username;

    private long pageIndex = 1;

    private long pageSize = 15;

    private Boolean isExpiredOrder;
}
