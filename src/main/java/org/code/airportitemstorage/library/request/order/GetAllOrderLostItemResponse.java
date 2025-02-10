package org.code.airportitemstorage.library.request.order;

import org.code.airportitemstorage.library.dto.order.OrderLostItemDto;

import java.util.List;

public class GetAllOrderLostItemResponse {
    public GetAllOrderLostItemResponse() {
    }

    public GetAllOrderLostItemResponse(List<OrderLostItemDto> orderLostItems, long total) {
        this.orderLostItems = orderLostItems;
        this.total = total;
    }

    public List<OrderLostItemDto> orderLostItems;

    public long total;
}
