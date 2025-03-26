package org.code.airportitemstorage.service.storageCabinet;

import org.code.airportitemstorage.library.entity.orders.Order;
import org.code.airportitemstorage.library.entity.storageCabinet.StorageCabinet;
import org.code.airportitemstorage.library.entity.user.User;
import org.code.airportitemstorage.library.request.storage.*;

public interface StorageCabinetService {
    GetStorageCabinetSettingResponse GetAllStorageCabinetSetting();

    int AddStorageCabinet(AddStorageCabinetRequest request);

    GetStorageCabinetsResponse GetStorageCabinetList(GetStorageCabinetsRequest request);

    int deleteStorageCabinetById(long id);

    int UpdateStorageCabinet(UpdateStorageCabinetRequest request);

    int UpdateStorageCabinetSetting(UpdateStorageCabinetSettingRequest request);

    OperateUserStorageCabinetResponse OperateUserStorageCabinet(OperateUserStorageCabinetRequest request) throws Exception;

    int AddStorageCategory(AddStorageCategoryRequest request);

    int deleteStorageCategoryById(long id);

    int UpdateStorageCategory(UpdateStorageCategoryRequest request);

    GetStorageCategoriesResponse GetStorageCategories(GetStorageCategoriesRequest request);

    void HandleDiscardedOrder(Order order, StorageCabinet storageCabinet, User user, long totalDate);
}
