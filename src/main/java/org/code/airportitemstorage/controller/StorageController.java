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

    @GetMapping("setting/get")
    public GetStorageCabinetSettingResponse getStorageCabinetSetting() {
        return storageCabinetService.GetAllStorageCabinetSetting();
    }

    @PostMapping("setting/update")
    public int updateStorageCabinetSetting(@RequestBody UpdateStorageCabinetSettingRequest request) {
        return storageCabinetService.UpdateStorageCabinetSetting(request);
    }

    @PostMapping("add")
    public int addStorageCabinet(@RequestBody AddStorageCabinetRequest request) {
        return storageCabinetService.AddStorageCabinet(request);
    }

    @GetMapping("list")
    public GetStorageCabinetsResponse getStorageCabinetList(GetStorageCabinetsRequest request){
        return storageCabinetService.GetStorageCabinetList(request);
    }

    @PostMapping("delete")
    public int deleteStorageCabinet(@RequestBody long id) {
        return storageCabinetService.deleteStorageCabinetById(id);
    }

    @PostMapping("update")
    public int updateStorageCabinet(@RequestBody UpdateStorageCabinetRequest request) {
        return storageCabinetService.UpdateStorageCabinet(request);
    }

    @PostMapping("operate")
    public OperateUserStorageCabinetResponse operateUserStorageCabinet(@RequestBody OperateUserStorageCabinetRequest request) throws Exception {
        return storageCabinetService.OperateUserStorageCabinet(request);
    }
}
