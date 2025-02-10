package org.code.airportitemstorage.library.dto.storage;

import lombok.Data;
import org.code.airportitemstorage.library.StorageCabinetSizeType;

import java.time.LocalDateTime;

@Data
public class StorageCabinetDto {
    private long id;

    private String num;

    private String name;

    private StorageCabinetSizeType sizeType;

    private boolean isStored;

    private LocalDateTime createdDate;

    public StorageCabinetDto(long id, String num, String name, StorageCabinetSizeType sizeType, boolean isStored, LocalDateTime createdDate) {
        this.id = id;
        this.num = num;
        this.name = name;
        this.sizeType = sizeType;
        this.isStored = isStored;
        this.createdDate = createdDate;
    }
}
