package org.code.airportitemstorage.serviceImpl.storageCabinet;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.code.airportitemstorage.library.OrderLogisticsStatus;
import org.code.airportitemstorage.library.OrderStorageStatus;
import org.code.airportitemstorage.library.RoleType;
import org.code.airportitemstorage.library.dto.storage.StorageCabinetDto;
import org.code.airportitemstorage.library.dto.storage.StorageCabinetSettingDto;
import org.code.airportitemstorage.library.dto.storage.StorageCategoryDto;
import org.code.airportitemstorage.library.entity.orders.Order;
import org.code.airportitemstorage.library.entity.orders.OrderLogistics;
import org.code.airportitemstorage.library.entity.storageCabinet.StorageCabinet;
import org.code.airportitemstorage.library.entity.storageCabinet.StorageCabinetSetting;
import org.code.airportitemstorage.library.entity.storageCabinet.StorageCategory;
import org.code.airportitemstorage.library.entity.user.User;
import org.code.airportitemstorage.library.entity.user.UserPoint;
import org.code.airportitemstorage.library.request.storage.*;
import org.code.airportitemstorage.mapper.order.OrderLogisticsMapper;
import org.code.airportitemstorage.mapper.order.OrderMapper;
import org.code.airportitemstorage.mapper.storageCabinet.StorageCabinetMapper;
import org.code.airportitemstorage.mapper.storageCabinet.StorageCabinetSettingMapper;
import org.code.airportitemstorage.mapper.storageCabinet.StorageCategoryMapper;
import org.code.airportitemstorage.mapper.users.UserPointMapper;
import org.code.airportitemstorage.service.order.OrderService;
import org.code.airportitemstorage.service.storageCabinet.StorageCabinetService;
import org.code.airportitemstorage.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StorageCabinetServiceImpl implements StorageCabinetService {
    private final StorageCabinetSettingMapper settingMapper;
    private final StorageCabinetMapper storageCabinetMapper;
    private final UserService userService;
    private final StorageCabinetSettingMapper storageCabinetSettingMapper;
    private final OrderMapper orderMapper;
    private final OrderService orderService;
    private final OrderLogisticsMapper orderLogisticsMapper;
    private final UserPointMapper userPointMapper;
    private final StorageCategoryMapper storageCategoryMapper;

    @Override
    public GetStorageCabinetSettingResponse GetAllStorageCabinetSetting() {
        List<StorageCabinetSetting> storageCabinetSettings = settingMapper.selectList(null);

        if (storageCabinetSettings.isEmpty()) return null;

        List<StorageCabinetSettingDto> settingList = storageCabinetSettings.stream()
                .map(StorageCabinetSettingDto::new)
                .collect(Collectors.toList());

        return new GetStorageCabinetSettingResponse(settingList);
    }

    @Override
    public int AddStorageCabinet(AddStorageCabinetRequest request) {
        StorageCabinet storageCabinet = new StorageCabinet();
        storageCabinet.setName(request.getName());
        storageCabinet.setNum(request.getNum());
        storageCabinet.setSizeType(request.getSizeType());
        storageCabinet.setCreatedDate(LocalDateTime.now());

        return storageCabinetMapper.insert(storageCabinet);
    }

    @Override
    public GetStorageCabinetsResponse GetStorageCabinetList(GetStorageCabinetsRequest request) {
        Page<StorageCabinet> storageCabinetPage = storageCabinetMapper.selectPage(
                new Page<>(request.pageIndex, request.pageSize),
                buildQueryWrapper(request)
        );

        List<StorageCabinetDto> storageCabinets = storageCabinetPage.getRecords()
                .stream()
                .map(item -> new StorageCabinetDto(
                        item.getId(),
                        item.getNum(),
                        item.getName(),
                        item.getSizeType(),
                        item.isStored(),
                        item.getCreatedDate()
                ))
                .collect(Collectors.toList());

        return new GetStorageCabinetsResponse(storageCabinets, storageCabinetPage.getTotal());
    }

    @Override
    public int deleteStorageCabinetById(long id) {
        StorageCabinet storageCabinet = storageCabinetMapper.selectById(id);
        if (storageCabinet == null) return 0;

        return storageCabinetMapper.deleteById(id);
    }

    @Override
    public int UpdateStorageCabinet(UpdateStorageCabinetRequest request) {
        StorageCabinet storageCabinet = storageCabinetMapper.selectById(request.getId());
        if (storageCabinet == null) return 0;

        storageCabinet.setName(request.getName());
        storageCabinet.setNum(request.getNum());
        storageCabinet.setSizeType(request.getSizeType());

        return storageCabinetMapper.updateById(storageCabinet);
    }

    @Override
    public int UpdateStorageCabinetSetting(UpdateStorageCabinetSettingRequest request) {
        User user = userService.CheckUserAuthorization();
        if (user == null) return 0;

        StorageCabinetSetting setting = storageCabinetSettingMapper.selectById(request.getId());
        if (setting == null) return 0;

        setting.setPrice(request.getPrice());

        return storageCabinetSettingMapper.updateById(setting);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OperateUserStorageCabinetResponse OperateUserStorageCabinet(OperateUserStorageCabinetRequest request) throws Exception {
        User user = userService.CheckUserAuthorization();
        if (user == null) return null;

        OperateUserStorageCabinetResponse response = new OperateUserStorageCabinetResponse();

        Order order = orderMapper.selectById(request.getOrderId());
        if (order == null) throw new Exception("No order found");

        StorageCabinet storageCabinet = storageCabinetMapper.selectById(order.getStorageCabinetId());

        var totalDate = Duration.between(order.getStorageTime(), LocalDateTime.now()).toSeconds();

        switch (request.operateType){
            case Discard -> HandleDiscardedOrder(order, storageCabinet, user, totalDate);
            case TakeOut -> {
                var storageDuration = Duration.between(order.getStorageTime(), LocalDateTime.now()).toSeconds();

                if(request.isLostItemOrder() && user.getRoleType() == RoleType.Admin){
                    order.setTotalStoredDuration(storageDuration);
                    order.setStorageStatus(OrderStorageStatus.TakenOut);
                    orderMapper.updateById(order);

                    storageCabinet.setStored(false);
                    storageCabinetMapper.updateById(storageCabinet);
                }else{
                    var querySettingWrapper = new QueryWrapper<StorageCabinetSetting>();
                    querySettingWrapper.eq("size_type", storageCabinet.getSizeType());

                    var storageCabinetSettings = storageCabinetSettingMapper.selectList(querySettingWrapper);

                    response.storageDuration = storageDuration;
                    response.totalPrice = orderService.CalculateTotalPrice(order, storageDuration, storageCabinetSettings);
                }
            }
            case Delivery ->{
                OrderLogistics orderLogistics = BuildOrderLogistics(request, user);
                orderLogisticsMapper.insert(orderLogistics);
            }
            default -> throw new IllegalArgumentException("Invalid status: " + request.operateType);
        }

        return response;
    }

    @Override
    public int AddStorageCategory(AddStorageCategoryRequest request) {
        if(request.getName().isEmpty() || userService.CheckUserAuthorization() == null) return 0;

        StorageCategory storageCategory = new StorageCategory();
        storageCategory.setCategoryName(request.getName());
        storageCategory.setCreatedDate(LocalDateTime.now());

        return storageCategoryMapper.insert(storageCategory);
    }

    @Override
    public int deleteStorageCategoryById(long id) {
        StorageCategory storageCategory = storageCategoryMapper.selectById(id);

        if (storageCategory == null) return 0;

        return storageCategoryMapper.deleteById(id);
    }

    @Override
    public int UpdateStorageCategory(UpdateStorageCategoryRequest request) {
        StorageCategory storageCategory = storageCategoryMapper.selectById(request.getId());

        if (storageCategory == null) return 0;

        storageCategory.setCategoryName(request.getName());

        return storageCategoryMapper.updateById(storageCategory);
    }

    @Override
    public GetStorageCategoriesResponse GetStorageCategories(GetStorageCategoriesRequest request) {
        QueryWrapper<StorageCategory> queryWrapper = new QueryWrapper<>();

        // 如果 isAll 为 true，则查询所有数据，否则分页查询
        Page<StorageCategory> storageCategoryPage;
        if (request.isAll()) {
            List<StorageCategory> allRecords = storageCategoryMapper.selectList(null);
            storageCategoryPage = new Page<>();
            storageCategoryPage.setRecords(allRecords);
            storageCategoryPage.setTotal(allRecords.size());
        } else {
            storageCategoryPage = storageCategoryMapper.selectPage(
                    new Page<>(request.getPageIndex(), request.getPageSize()),
                    null
            );
        }

        List<StorageCategoryDto> storageCategories = storageCategoryPage.getRecords()
                .stream()
                .map(item -> new StorageCategoryDto(
                        item.getId(),
                        item.getCategoryName(),
                        item.getCreatedDate()
                ))
                .collect(Collectors.toList());

        return new GetStorageCategoriesResponse(storageCategories, storageCategoryPage.getTotal());
    }

    private static OrderLogistics BuildOrderLogistics(OperateUserStorageCabinetRequest request, User user) {
        OrderLogistics orderLogistics = new OrderLogistics();
        orderLogistics.setOrderId(request.getOrderId());
        orderLogistics.setRecipient(request.userOrderLogistics.getRecipient());
        orderLogistics.setPhone(request.userOrderLogistics.getPhone());
        orderLogistics.setDeliveryAddress(request.userOrderLogistics.getDeliveryAddress());
        orderLogistics.setPaymentMethod(request.userOrderLogistics.getPaymentMethod());
        orderLogistics.setStatus(OrderLogisticsStatus.Pending);
        orderLogistics.setUserId(user.getId());
        return orderLogistics;
    }

    private QueryWrapper<StorageCabinet> buildQueryWrapper(GetStorageCabinetsRequest request) {
        var queryWrapper = new QueryWrapper<StorageCabinet>();

        if(request.getName() != null && !request.getName().isEmpty())
            queryWrapper.like("name", request.getName());

        if(request.sortByStored != null && !request.getSortByStored().isEmpty()){
            if(request.getSortByStored().equals("asc"))
                queryWrapper.orderByAsc("is_stored");
            if(request.getSortByStored().equals("desc"))
                queryWrapper.orderByDesc("is_stored");
        }

        if (request.getSortBySize() != null && !request.getSortBySize().isEmpty()) {
            String orderCase = "(CASE size_type " +
                    "WHEN 'Small' THEN 1 " +
                    "WHEN 'Medium' THEN 2 " +
                    "WHEN 'Large' THEN 3 " +
                    "END)";

            if (request.getSortBySize().equalsIgnoreCase("asc")) {
                queryWrapper.orderByAsc(orderCase);
            } else if (request.getSortBySize().equalsIgnoreCase("desc")) {
                queryWrapper.orderByDesc(orderCase);
            }
        }

        return queryWrapper;
    }

    public void HandleDiscardedOrder(Order order, StorageCabinet storageCabinet, User user, long totalDate){
        float totalPrice = orderService.HandleOrderTotalPrice(storageCabinet.getSizeType(), order, totalDate);

        // 更新订单信息
        order.setPrice(totalPrice);
        order.setStorageStatus(OrderStorageStatus.Discarded);
        order.setTotalStoredDuration(totalDate);
        storageCabinet.setRequiredClean(true);

        // 扣除积分
        var queryUserPointWrapper = new QueryWrapper<UserPoint>();
        queryUserPointWrapper.eq("user_id", user.getId());
        UserPoint userPoint = userPointMapper.selectOne(queryUserPointWrapper);
        userPoint.setPoint(userPoint.getPoint() - totalPrice);

        orderMapper.updateById(order);
        storageCabinetMapper.updateById(storageCabinet);
    }
}
