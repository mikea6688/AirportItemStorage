package org.code.airportitemstorage.service.storageCabinet;

import org.code.airportitemstorage.library.request.storage.*;

public interface StorageCabinetService {
    GetStorageCabinetSettingResponse GetAllStorageCabinetSetting();

    int AddStorageCabinet(AddStorageCabinetRequest request);

    GetStorageCabinetsResponse GetStorageCabinetList(GetStorageCabinetsRequest request);

    int deleteStorageCabinetById(long id);

    int UpdateStorageCabinet(UpdateStorageCabinetRequest request);

    int UpdateStorageCabinetSetting(UpdateStorageCabinetSettingRequest request);

    OperateUserStorageCabinetResponse OperateUserStorageCabinet(OperateUserStorageCabinetRequest request) throws Exception;
}
