package org.code.airportitemstorage.library.dto.storage;

import lombok.Data;
import org.code.airportitemstorage.library.StorageCabinetSizeType;
import org.code.airportitemstorage.library.StorageDateType;
import org.code.airportitemstorage.library.entity.storageCabinet.StorageCabinetSetting;

@Data
public class StorageCabinetSettingDto {
    private long id;

    private StorageCabinetSizeType sizeType;

    private String height;

    private String width;

    private String length;

    private StorageDateType dateType;

    private float price;

    public StorageCabinetSettingDto(StorageCabinetSetting setting) {
        this.id = setting.getId();
        this.sizeType = setting.getSizeType();
        this.height = setting.getHeight();
        this.width = setting.getWidth();
        this.length = setting.getLength();
        this.dateType = setting.getDateType();
        this.price = setting.getPrice();
    }
}
