package org.code.airportitemstorage.library.dto.order;

import lombok.Data;
import org.code.airportitemstorage.library.PaymentMethod;

@Data
public class UserOrderLogisticsDto {
    public long id;

    public long orderId;

    public String recipient;

    public String phone;

    public String deliveryAddress;

    public PaymentMethod paymentMethod;
}
