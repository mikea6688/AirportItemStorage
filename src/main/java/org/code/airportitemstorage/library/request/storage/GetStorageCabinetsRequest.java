package org.code.airportitemstorage.library.request.storage;

import lombok.Data;

@Data
public class GetStorageCabinetsRequest {
    public String name;

    public String sortBySize;

    public String sortByStored;

    public long pageIndex = 1;

    public long pageSize = 15;
}
