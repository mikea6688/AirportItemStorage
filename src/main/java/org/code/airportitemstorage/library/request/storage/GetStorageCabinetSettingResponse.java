package org.code.airportitemstorage.library.request.storage;

import org.code.airportitemstorage.library.dto.storage.StorageCabinetSettingDto;

import java.util.List;

public class GetStorageCabinetSettingResponse {

    public GetStorageCabinetSettingResponse(List<StorageCabinetSettingDto> settingList) {
        this.settingList = settingList;
    }

    public List<StorageCabinetSettingDto> settingList;
}
