package org.code.airportitemstorage.library.dto.storage;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StorageCategoryDto {
    private long id;

    private String categoryName;

    private LocalDateTime createdDate;

    public StorageCategoryDto(long id, String categoryName, LocalDateTime createdDate) {
        this.id = id;
        this.categoryName = categoryName;
        this.createdDate = createdDate;
    }
}
