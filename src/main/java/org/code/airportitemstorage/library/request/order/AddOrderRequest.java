package org.code.airportitemstorage.library.request.order;

import lombok.Data;
import org.code.airportitemstorage.library.StorageCabinetSizeType;
import org.code.airportitemstorage.library.StorageDateType;

@Data
public class AddOrderRequest {
    public StorageCabinetSizeType sizeType;

    public StorageDateType dateType;

    public int monthCount;

    public String name;

    public boolean isValuable;

    public boolean useMemberRenewalService;

    public float estimatedPrice;

    public boolean isLostItem;
}
