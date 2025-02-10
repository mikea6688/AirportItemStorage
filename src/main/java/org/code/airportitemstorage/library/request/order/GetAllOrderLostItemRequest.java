package org.code.airportitemstorage.library.request.order;

import lombok.Data;
import org.code.airportitemstorage.library.StorageCabinetSizeType;

@Data
public class GetAllOrderLostItemRequest {
    public String lostItemName;

    public StorageCabinetSizeType sizeType;

    public long pageIndex = 1;

    public long pageSize = 15;
}
