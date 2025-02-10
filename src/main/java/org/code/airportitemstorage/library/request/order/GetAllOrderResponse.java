package org.code.airportitemstorage.library.request.order;

import lombok.Data;
import org.code.airportitemstorage.library.dto.order.UserOrderDto;

import java.util.List;

@Data
public class GetAllOrderResponse {
    public List<UserOrderDto> orders;

    public long total;
}
