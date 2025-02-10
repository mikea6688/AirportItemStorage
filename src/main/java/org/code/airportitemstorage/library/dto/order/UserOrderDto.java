package org.code.airportitemstorage.library.dto.order;

import lombok.Data;
import org.code.airportitemstorage.library.OrderStorageStatus;

import java.time.LocalDateTime;

@Data
public class UserOrderDto {
    public Long id;

    public String num;

    public String username;

    public LocalDateTime storageDate;

    public long storedDuration;

    public String voucherNumber;

    public float storagePrice;

    public boolean isPayment;

    public OrderStorageStatus status;
}
