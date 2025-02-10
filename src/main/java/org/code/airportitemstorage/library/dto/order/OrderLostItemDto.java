package org.code.airportitemstorage.library.dto.order;

import lombok.Data;
import org.code.airportitemstorage.library.OrderStorageStatus;
import org.code.airportitemstorage.library.StorageCabinetSizeType;

import java.time.LocalDateTime;

@Data
public class OrderLostItemDto {
    public long orderId;

    public String cabinetNumber;

    public StorageCabinetSizeType sizeType;

    public String name;

    public LocalDateTime storageTime;

    public LocalDateTime endTime;

    public String storageName;

    public OrderStorageStatus storageStatus;
}
