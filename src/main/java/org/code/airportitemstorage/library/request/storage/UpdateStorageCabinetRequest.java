package org.code.airportitemstorage.library.request.storage;

import lombok.Data;
import org.code.airportitemstorage.library.StorageCabinetSizeType;

@Data
public class UpdateStorageCabinetRequest {
    public long id;

    public String num;

    public StorageCabinetSizeType sizeType;

    public String name;
}
