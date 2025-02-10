package org.code.airportitemstorage.library.request.storage;

import lombok.Data;
import org.code.airportitemstorage.library.StorageCabinetSizeType;

@Data
public class AddStorageCabinetRequest {
    public String num;
    public String name;
    public StorageCabinetSizeType sizeType;
}
