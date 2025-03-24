package org.code.airportitemstorage.controller;

import org.code.airportitemstorage.library.request.storage.*;
import org.code.airportitemstorage.service.storageCabinet.StorageCabinetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/storage")
public class StorageController {

    @Autowired
    private StorageCabinetService storageCabinetService;
//存储存储柜的配置信息
    @GetMapping("setting/get")
    public GetStorageCabinetSettingResponse getStorageCabinetSetting() {
        return storageCabinetService.GetAllStorageCabinetSetting();
    }
//更新存储柜配置信息
    @PostMapping("setting/update")
    public int updateStorageCabinetSetting(@RequestBody UpdateStorageCabinetSettingRequest request) {
        return storageCabinetService.UpdateStorageCabinetSetting(request);
    }
//添加空闲存储柜信息
    @PostMapping("add")
    public int addStorageCabinet(@RequestBody AddStorageCabinetRequest request) {
        return storageCabinetService.AddStorageCabinet(request);
    }
//获取所有存储柜的信息
    @GetMapping("list")
    public GetStorageCabinetsResponse getStorageCabinetList(GetStorageCabinetsRequest request){
        return storageCabinetService.GetStorageCabinetList(request);
    }
//删除存储柜信息
    @PostMapping("delete")
    public int deleteStorageCabinet(@RequestBody long id) {
        return storageCabinetService.deleteStorageCabinetById(id);
    }
//更新存储柜信息
    @PostMapping("update")
    public int updateStorageCabinet(@RequestBody UpdateStorageCabinetRequest request) {
        return storageCabinetService.UpdateStorageCabinet(request);
    }
//操作用户存储柜
    @PostMapping("operate")
    public OperateUserStorageCabinetResponse operateUserStorageCabinet(@RequestBody OperateUserStorageCabinetRequest request) throws Exception {
        return storageCabinetService.OperateUserStorageCabinet(request);
    }

    @PostMapping("category/add")
    public int addStorageCategory(@RequestBody AddStorageCategoryRequest request) {
        return storageCabinetService.AddStorageCategory(request);
    }

    @PostMapping("category/delete")
    public int deleteStorageCategory(@RequestBody long id) {
        return storageCabinetService.deleteStorageCategoryById(id);
    }

    @PostMapping("category/update")
    public int updateStorageCategory(@RequestBody UpdateStorageCategoryRequest request) {
        return storageCabinetService.UpdateStorageCategory(request);
    }

    @GetMapping("category/list")
    public GetStorageCategoriesResponse getStorageCategories(GetStorageCategoriesRequest request) {
        return storageCabinetService.GetStorageCategories(request);
    }
}
