package org.code.airportitemstorage.library.request.storage;

import lombok.Data;
import org.code.airportitemstorage.library.dto.storage.StorageCategoryDto;

import java.util.List;

@Data
public class GetStorageCategoriesResponse {
    public List<StorageCategoryDto> storageCategories;

    public long total;

    public GetStorageCategoriesResponse(List<StorageCategoryDto> storageCategories, long total) {
        this.storageCategories = storageCategories;
        this.total = total;
    }
}
