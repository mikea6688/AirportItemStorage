package org.code.airportitemstorage.library.request.order;

import lombok.Data;

@Data
public class GetAllOrderRequest {
    public String cabinetNumber;

    public String username;

    public long pageIndex = 1;

    public long pageSize = 15;
}
