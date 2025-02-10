package org.code.airportitemstorage.library.dto.order;

import lombok.Data;
import org.code.airportitemstorage.library.OrderLogisticsStatus;

import java.time.LocalDateTime;

@Data
public class OrderLogisticsDto {
    public long id;
    public String storageCabinetNumber;
    public String storageUserAccount;
    public String recipient;
    public String phone;
    public String deliveryAddress;
    public float paymentAmount;
    public LocalDateTime storageTime;
    public boolean isTakenOut;
    public OrderLogisticsStatus logisticsStatus;
}
