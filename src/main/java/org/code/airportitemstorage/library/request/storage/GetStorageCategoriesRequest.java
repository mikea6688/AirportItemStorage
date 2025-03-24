package org.code.airportitemstorage.library.request.storage;

import lombok.Data;

@Data
public class GetStorageCategoriesRequest {
    public long pageIndex = 1;

    public long pageSize = 15;
}
