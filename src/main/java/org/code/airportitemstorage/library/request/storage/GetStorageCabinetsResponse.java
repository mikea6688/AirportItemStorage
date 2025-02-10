package org.code.airportitemstorage.library.request.storage;

import org.code.airportitemstorage.library.dto.storage.StorageCabinetDto;

import java.util.List;

public class GetStorageCabinetsResponse {
    public GetStorageCabinetsResponse(List<StorageCabinetDto> cabinets, long total) {
        this.cabinets = cabinets;
        this.total = total;
    }

    public List<StorageCabinetDto> cabinets;

    public long total;
}
