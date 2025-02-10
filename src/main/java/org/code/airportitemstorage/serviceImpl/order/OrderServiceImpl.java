package org.code.airportitemstorage.serviceImpl.order;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.code.airportitemstorage.library.OrderStorageStatus;
import org.code.airportitemstorage.library.RoleType;
import org.code.airportitemstorage.library.StorageCabinetSizeType;
import org.code.airportitemstorage.library.StorageDateType;
import org.code.airportitemstorage.library.dto.order.OrderLogisticsDto;
import org.code.airportitemstorage.library.dto.order.OrderLostItemDto;
import org.code.airportitemstorage.library.dto.order.OrderStatisticalDto;
import org.code.airportitemstorage.library.dto.order.UserOrderDto;
import org.code.airportitemstorage.library.entity.orders.Order;
import org.code.airportitemstorage.library.entity.orders.OrderLogistics;
import org.code.airportitemstorage.library.entity.orders.OrderPaySuccessRecord;
import org.code.airportitemstorage.library.entity.storageCabinet.StorageCabinet;
import org.code.airportitemstorage.library.entity.storageCabinet.StorageCabinetSetting;
import org.code.airportitemstorage.library.entity.storageCabinet.UserVoucherNumber;
import org.code.airportitemstorage.library.entity.user.User;
import org.code.airportitemstorage.library.entity.user.UserPoint;
import org.code.airportitemstorage.library.request.order.*;
import org.code.airportitemstorage.mapper.order.OrderLogisticsMapper;
import org.code.airportitemstorage.mapper.order.OrderMapper;
import org.code.airportitemstorage.mapper.order.OrderPaySuccessRecordMapper;
import org.code.airportitemstorage.mapper.storageCabinet.StorageCabinetMapper;
import org.code.airportitemstorage.mapper.storageCabinet.StorageCabinetSettingMapper;
import org.code.airportitemstorage.mapper.users.UserMapper;
import org.code.airportitemstorage.mapper.users.UserPointMapper;
import org.code.airportitemstorage.mapper.users.UserVoucherNumberMapper;
import org.code.airportitemstorage.service.UniqueNumberService;
import org.code.airportitemstorage.service.order.OrderService;
import org.code.airportitemstorage.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final UserService userService;
    private final UniqueNumberService uniqueNumberService;
    private final StorageCabinetMapper storageCabinetMapper;
    private final UserVoucherNumberMapper userVoucherNumberMapper;
    private final StorageCabinetSettingMapper storageCabinetSettingMapper;
    private final UserPointMapper userPointMapper;
    private final OrderLogisticsMapper orderLogisticsMapper;
    private final OrderPaySuccessRecordMapper orderPaySuccessRecordMapper;
    private final UserMapper userMapper;

    private static final int OrderThresholdForVIP = 10;

    @Override
    @Transactional(rollbackFor = Exception.class) // ������������쳣�Զ��ع�
    public int AddUserOrder(AddOrderRequest request) throws Exception {
        try{
            User user = userService.CheckUserAuthorization();

            if (user == null) return 0;

            var querySettingWrapper = new QueryWrapper<StorageCabinetSetting>();
            querySettingWrapper.eq("size_type", request.sizeType).eq("date_type", request.dateType);

            StorageCabinetSetting storageCabinetSetting = storageCabinetSettingMapper.selectOne(querySettingWrapper);
            if (storageCabinetSetting == null) throw new Exception("No storage cabinet setting found");
            var estimatedPrice = CalculateOrderEstimatedPrice(request.getMonthCount(), storageCabinetSetting.getPrice(), storageCabinetSetting.getDateType());

            if(CheckUserPoint(user.getId())) throw new Exception("Cannot be stored when user point is negative.");

            UserVoucherNumber userVoucherNumber = GenerateUserVoucherNumber(user.getId());

            StorageCabinet storageCabinet = DistributeVacantStorageCabinet(request.sizeType);
            if (storageCabinet == null) throw new Exception("Please operate later, not found vacant storage cabinet.");
            storageCabinet.setStored(true);

            Order order = new Order();
            order.setPrice(0);
            order.setUserId(user.getId());
            order.setName(request.getName());
            order.setValuable(request.isValuable());
            order.setEstimatedPrice(estimatedPrice);
            order.setStorageTime(LocalDateTime.now());
            order.setStorageStatus(OrderStorageStatus.Using);
            order.setStorageCabinetId(storageCabinet.getId());
            order.setVoucherNumber(userVoucherNumber.getVoucherNumber());
            order.setUseMemberRenewalService(request.isUseMemberRenewalService());
            order.setMonthCount(request.getMonthCount());
            order.setDateType(request.getDateType());
            order.setLostItem(request.isLostItem());
            if(orderMapper.insert(order) == 0)throw new Exception("Order already exists.");

            if(userVoucherNumberMapper.insert(userVoucherNumber) == 0)throw new Exception("Voucher number already exists.");

            if(storageCabinetMapper.updateById(storageCabinet) == 0)throw new Exception("Storage cabinet already exists.");

            return 1;
        }
        catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }

    private boolean CheckUserPoint(long userId) {
        var queryUserPointWrapper = new QueryWrapper<UserPoint>();
        queryUserPointWrapper.eq("user_id", userId);
        List<UserPoint> userPoints = userPointMapper.selectList(queryUserPointWrapper);

        UserPoint userPoint = userPoints.stream().findFirst().orElse(null);

        return userPoint == null || !(userPoint.getPoint() >= 0);
    }

    @Override
    public GetUserOrderResponse GetUserOrderList(GetUserOrderRequest request) {
        User user = userService.CheckUserAuthorization();
        if (user == null) return null;

        ArrayList<UserOrderDto> orderDtoList = new ArrayList<>();

        var page = new Page<Order>((request.pageIndex - 1) * request.pageSize, request.pageSize);

        var queryOrderWrapper = new QueryWrapper<Order>();
        queryOrderWrapper.eq("user_id", user.getId()).eq("is_lost_item", false);

        List<Order> orderList = orderMapper.selectList(queryOrderWrapper);

        if(request.num != null && !request.getNum().isEmpty()){
            var cabinetIds = orderList.stream().map(Order::getStorageCabinetId).toList();
            var queryCabinetWrapper = new QueryWrapper<StorageCabinet>();
            queryCabinetWrapper.in("id", cabinetIds).eq("num", request.getNum()).last("limit 1");
            StorageCabinet storageCabinet = storageCabinetMapper.selectOne(queryCabinetWrapper);
            queryOrderWrapper.eq("storage_cabinet_id", storageCabinet.getId());
        }

        if(request.status != null && request.getStatus() != null){
            queryOrderWrapper.eq("storage_status", request.getStatus());
        }

        Page<Order> orderPage = orderMapper.selectPage(page, queryOrderWrapper);

        List<Long> cabinetIds = orderPage.getRecords().stream().map(Order::getStorageCabinetId).toList();

        if(cabinetIds.isEmpty())return new GetUserOrderResponse();

        List<StorageCabinet> storageCabinets = storageCabinetMapper.selectBatchIds(cabinetIds);

        for(Order order : orderPage.getRecords()){
            StorageCabinet storageCabinet = storageCabinets.stream().filter(x -> x.getId() == order.getStorageCabinetId()).findFirst().orElse(null);
            if(storageCabinet == null) continue;

            long storedDuration =  order.getTotalStoredDuration();
            if(order.getStorageStatus() == OrderStorageStatus.Using){
                storedDuration = Duration.between(order.getStorageTime(), LocalDateTime.now()).toSeconds();
            }

            var storagePrice = order.getPrice() == 0 ? order.getEstimatedPrice() : order.getPrice();

            UserOrderDto dto = new UserOrderDto();
            dto.setId(order.getId());
            dto.setNum(storageCabinet.getNum());
            dto.setStatus(order.getStorageStatus());
            dto.setVoucherNumber(order.getVoucherNumber());
            dto.setStorageDate(order.getStorageTime());
            dto.setStoredDuration(storedDuration);
            dto.setStoragePrice(storagePrice);
            orderDtoList.add(dto);
        }

        GetUserOrderResponse response = new GetUserOrderResponse();

        response.setOrders(orderDtoList);
        response.total = orderPage.getTotal();

        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean PayUserOrder(PayUserOrderRequest request) throws Exception {
        User user = userService.CheckUserAuthorization();
        if (user == null) return false;

        var queryUserPointWrapper = new QueryWrapper<UserPoint>();
        queryUserPointWrapper.eq("user_id", user.getId());

        Order order = orderMapper.selectById(request.getOrderId());
        if(order == null) throw new Exception("Not found order.");
        if(order.getStorageStatus() != OrderStorageStatus.Using)throw new Exception("Order storage item has taken out.");

        StorageCabinet storageCabinet = storageCabinetMapper.selectById(order.getStorageCabinetId());

        var querySettingWrapper = new QueryWrapper<StorageCabinetSetting>();
        querySettingWrapper.eq("size_type", storageCabinet.getSizeType());

        var storageCabinetSettings = storageCabinetSettingMapper.selectList(querySettingWrapper);

        var storageDuration = Duration.between(order.getStorageTime(), LocalDateTime.now()).toSeconds();

        float totalPrice = CalculateTotalPrice(order, storageDuration, storageCabinetSettings);

        // ���¶�����Ϣ
        order.setPrice(totalPrice);
        order.setTotalStoredDuration(storageDuration);
        order.setStorageStatus(OrderStorageStatus.TakenOut);

        // �۳�����
        UserPoint userPoint = userPointMapper.selectOne(queryUserPointWrapper);
        userPoint.setPoint(userPoint.getPoint() - totalPrice);

        // ���¹��Ӵ洢״̬
        storageCabinet.setStored(false);

        if(orderMapper.updateById(order) == 0 ||
                userPointMapper.updateById(userPoint) == 0 ||
                storageCabinetMapper.updateById(storageCabinet) == 0 ||
                orderPaySuccessRecordMapper.insert(new OrderPaySuccessRecord(order.getId(), user.getId())) == 0)
            throw new Exception("Update failed, Please to operate later.");

        // �ж��Ƿ��ܳ�ΪVIP
        CheckUserMeetsRequirementsForVip(user);

        return true;
    }

    private void CheckUserMeetsRequirementsForVip(User user) {
        var queryWrapper = new QueryWrapper<OrderPaySuccessRecord>();
        queryWrapper.eq("user_id", user.getId());

        Long orderCount = orderPaySuccessRecordMapper.selectCount(queryWrapper);

        if(orderCount >= OrderThresholdForVIP){
            user.setRoleType(RoleType.VIP);
            userMapper.updateById(user);
        }
    }

    @Override
    public float CalculateTotalPrice(Order order, long storageDuration, List<StorageCabinetSetting> cabinetSettings) {
        float estimatedPrice = order.getEstimatedPrice();

        // Ԥ�ڵ�ʱ��
        long estimatedTime = getEstimatedTime(order.getDateType());

        // ����ѹ���ʱ�����Ԥ��ʱ�䣬�������
        if (storageDuration > estimatedTime) {
            long diffInDays = calculateDaysDifference(storageDuration, estimatedTime);

            // ��ȡ��Ӧ�������͵Ĺ�������
            StorageCabinetSetting cabinetSetting = getCabinetSettingByDays(diffInDays, cabinetSettings);

            // ���û���ҵ���Ӧ�Ĺ������ã��򷵻�ԭ��
            if (cabinetSetting == null) return estimatedPrice;

            // ���ݳ�����������������
            estimatedPrice = calculatePriceBasedOnDiff(diffInDays, cabinetSetting, estimatedPrice);
        }

        return estimatedPrice;
    }

    @Override
    public GetOrderLogisticsInfoResponse GetOrderLogisticsInfo(GetOrderLogisticsInfoRequest request) {
        User user = userService.CheckUserAuthorization();
        if(user == null) return null;

        ArrayList<OrderLogisticsDto> orderLogisticsList = new ArrayList<>();

        var page = new Page<OrderLogistics>((request.pageIndex - 1) * request.pageSize, request.pageSize);

        var queryWrapper = new QueryWrapper<OrderLogistics>();
        queryWrapper.eq("user_id", user.getId());
        Page<OrderLogistics> orderLogisticsPage = orderLogisticsMapper.selectPage(page, queryWrapper);

        if(orderLogisticsPage == null || orderLogisticsPage.getRecords().isEmpty()) return null;

        var orderIds = orderLogisticsPage.getRecords().stream().map(OrderLogistics::getOrderId).toList();

        List<Order> orders = orderMapper.selectBatchIds(orderIds);

        List<Long> cabinetIds = orders.stream().map(Order::getStorageCabinetId).toList();

        List<StorageCabinet> storageCabinets = storageCabinetMapper.selectBatchIds(cabinetIds);

        for (OrderLogistics orderLogistics : orderLogisticsPage.getRecords()) {
            Order order = orders.stream().filter(x -> x.getId() == orderLogistics.getOrderId()).findFirst().orElse(null);
            if(order == null) continue;
            StorageCabinet cabinet = storageCabinets.stream().filter(x->x.getId() == order.getStorageCabinetId()).findFirst().orElse(null);
            if(cabinet == null) continue;

            OrderLogisticsDto dto = new OrderLogisticsDto();
            dto.setId(orderLogistics.getId());
            dto.setLogisticsStatus(orderLogistics.getStatus());
            dto.setStorageTime(order.getStorageTime());
            dto.setPaymentAmount(order.getEstimatedPrice());
            dto.setTakenOut(order.getStorageStatus() == OrderStorageStatus.TakenOut);
            dto.setStorageCabinetNumber(cabinet.getNum());

            orderLogisticsList.add(dto);
        }

        return new GetOrderLogisticsInfoResponse(orderLogisticsList, orderLogisticsPage.getTotal());
    }

    @Override
    public GetAllOrderResponse GetAllOrder(GetAllOrderRequest request) {
        ArrayList<UserOrderDto> orderDtoList = new ArrayList<>();

        var page = new Page<Order>((request.pageIndex - 1) * request.pageSize, request.pageSize);

        List<Order> orderList = orderMapper.selectList(null);

        var queryOrderWrapper = new QueryWrapper<Order>();
        queryOrderWrapper.eq("is_lost_item", false);

        if(request.cabinetNumber != null && !request.getCabinetNumber().isEmpty()){
            var cabinetIds = orderList.stream().map(Order::getStorageCabinetId).toList();

            var queryCabinetWrapper = new QueryWrapper<StorageCabinet>();
            queryCabinetWrapper.in("id", cabinetIds).eq("num", request.getCabinetNumber()).last("limit 1");
            StorageCabinet storageCabinet = storageCabinetMapper.selectOne(queryCabinetWrapper);

            queryOrderWrapper.eq("storage_cabinet_id", storageCabinet.getId());
        }

        if(request.username != null && request.getUsername() != null){
            var queryUserWrapper = new QueryWrapper<User>();
            queryUserWrapper.eq("nick_name", request.getUsername());
            User user = userMapper.selectOne(queryUserWrapper);

            queryOrderWrapper.eq("user_id", user.getId());
        }

        Page<Order> orderPage = orderMapper.selectPage(page, queryOrderWrapper);

        if(orderPage == null) return null;

        var orderIds = orderPage.getRecords().stream().map(Order::getId).toList();
        var userIds = orderPage.getRecords().stream().map(Order::getUserId).toList();

        // get cabinet
        List<Long> cabinetIds = orderPage.getRecords().stream().map(Order::getStorageCabinetId).toList();
        if(cabinetIds.isEmpty())return new GetAllOrderResponse();
        var storageCabinets = storageCabinetMapper.selectBatchIds(cabinetIds);

        // get paySuccessOrder
        var queryPaySuccessRecordWrapper = new QueryWrapper<OrderPaySuccessRecord>();
        queryPaySuccessRecordWrapper.in("order_id", orderIds);
        var orderPaySuccessRecords = orderPaySuccessRecordMapper.selectList(queryPaySuccessRecordWrapper);

        // get users
        var userQueryWrapper = new QueryWrapper<User>();
        userQueryWrapper.in("id", userIds);
        var users = userMapper.selectList(userQueryWrapper);

        for(Order order : orderPage.getRecords()){
            var storageCabinet = storageCabinets.stream().filter(x -> x.getId() == order.getStorageCabinetId()).findFirst().orElse(null);

            var user = users.stream().filter(x -> x.getId() == order.getUserId()).findFirst().orElse(null);

            if(storageCabinet == null || user == null) continue;

            var orderPaySuccessRecord = orderPaySuccessRecords.stream().filter(x -> x.getId() == order.getId()).findFirst().orElse(null);

            UserOrderDto dto = new UserOrderDto();
            dto.setId(order.getId());
            dto.setNum(storageCabinet.getNum());
            dto.setStatus(order.getStorageStatus());
            dto.setVoucherNumber(order.getVoucherNumber());
            dto.setStorageDate(order.getStorageTime());
            dto.setStoredDuration(Duration.between(order.getStorageTime(), LocalDateTime.now()).toSeconds());
            dto.setStoragePrice(order.getPrice() == 0 ? order.getEstimatedPrice() : order.getPrice());
            dto.setUsername(user.getAccountName());
            dto.setPayment(orderPaySuccessRecord != null);
            orderDtoList.add(dto);
        }

        var response = new GetAllOrderResponse();

        response.setOrders(orderDtoList);
        response.total = orderPage.getTotal();

        return response;
    }

    @Override
    public GetAllOrderLogisticsListResponse GetAllOrderLogisticsList(GetAllOrderLogisticsListRequest request) {
        ArrayList<OrderLogisticsDto> orderLogisticsList = new ArrayList<>();

        var page = new Page<OrderLogistics>((request.pageIndex - 1) * request.pageSize, request.pageSize);

        var queryOrderLogisticsListWrapper = new QueryWrapper<OrderLogistics>();

        if(request.storageUserAccount != null && !request.storageUserAccount.isEmpty()){
            var queryUserWrapper = new QueryWrapper<User>();
            queryUserWrapper.eq("account_name", request.storageUserAccount);
            User user = userMapper.selectOne(queryUserWrapper);

            queryOrderLogisticsListWrapper.eq("user_id", user.getId());
        }

        if(request.cabinetNumber != null && !request.cabinetNumber.isEmpty()){
            var queryStorageCabinetWrapper = new QueryWrapper<StorageCabinet>();
            queryStorageCabinetWrapper.eq("num", request.cabinetNumber);
            StorageCabinet storageCabinet = storageCabinetMapper.selectOne(queryStorageCabinetWrapper);

            var queryOrderWrapper = new QueryWrapper<Order>();
            queryOrderWrapper.eq("storage_cabinet_id", storageCabinet.getId());
            List<Long> orderIds = orderMapper.selectList(queryOrderWrapper).stream().map(Order::getId).toList();

            queryOrderLogisticsListWrapper.in("order_id", orderIds);
        }

        Page<OrderLogistics> logisticsPage = orderLogisticsMapper.selectPage(page, queryOrderLogisticsListWrapper);
        if(logisticsPage.getRecords().isEmpty())return new GetAllOrderLogisticsListResponse();

        List<Long> userIds = logisticsPage.getRecords().stream().map(OrderLogistics::getUserId).toList();
        List<User> users = userMapper.selectBatchIds(userIds);

        List<Long> orderIds = logisticsPage.getRecords().stream().map(OrderLogistics::getOrderId).toList();
        List<Order> orders = orderMapper.selectBatchIds(orderIds);
        var cabinetIds = orders.stream().map(Order::getStorageCabinetId).toList();
        List<StorageCabinet> storageCabinets = storageCabinetMapper.selectBatchIds(cabinetIds);

        for (OrderLogistics logistics : logisticsPage.getRecords()) {
            var user = users.stream().filter(x -> x.getId() == logistics.getUserId()).findFirst().orElse(null);
            if(user == null) continue;
            Order order = orders.stream().filter(x -> x.getId() == logistics.getOrderId()).findFirst().orElse(null);
            if(order == null) continue;
            StorageCabinet storageCabinet = storageCabinets.stream().filter(x -> x.getId() == order.getStorageCabinetId()).findFirst().orElse(null);
            if(storageCabinet == null) continue;

            OrderLogisticsDto dto = new OrderLogisticsDto();
            dto.setId(logistics.getId());
            dto.setStorageCabinetNumber(storageCabinet.getNum());
            dto.setStorageUserAccount(user.getAccountName());
            dto.setRecipient(logistics.getRecipient());
            dto.setPhone(logistics.getPhone());
            dto.setDeliveryAddress(logistics.getDeliveryAddress());

            orderLogisticsList.add(dto);
        }

        return new GetAllOrderLogisticsListResponse(orderLogisticsList, logisticsPage.getTotal());
    }

    @Override
    public GetAllOrderLostItemResponse GetAllOrderLostItemList(GetAllOrderLostItemRequest request) {
        ArrayList<OrderLostItemDto> orderLostItemList = new ArrayList<>();

        var page = new Page<Order>((request.pageIndex - 1) * request.pageSize, request.pageSize);

        var queryOrderWrapper = new QueryWrapper<Order>();
        queryOrderWrapper.eq("is_lost_item", true);

        if(request.lostItemName != null && !request.getLostItemName().isEmpty()){
            queryOrderWrapper.eq("name", request.lostItemName);
        }

        var queryStorageCabinetWrapper = new QueryWrapper<StorageCabinet>();
        queryStorageCabinetWrapper.eq("size_type", request.sizeType);
        List<StorageCabinet> storageCabinetsForQuery = storageCabinetMapper.selectList(queryStorageCabinetWrapper);
        if(!storageCabinetsForQuery.isEmpty()){
            List<Long> cabinetIdsForQuery = storageCabinetsForQuery.stream().map(StorageCabinet::getId).toList();
            if(!cabinetIdsForQuery.isEmpty()){
                queryOrderWrapper.in("storage_cabinet_id", cabinetIdsForQuery);
            }
        }

        var orderPage = orderMapper.selectPage(page, queryOrderWrapper);
        if (orderPage.getRecords().isEmpty()) return new GetAllOrderLostItemResponse();

        List<Long> cabinetIds = orderPage.getRecords().stream().map(Order::getStorageCabinetId).toList();
        List<StorageCabinet> storageCabinets = storageCabinetMapper.selectBatchIds(cabinetIds);

        List<Long> userIds = orderPage.getRecords().stream().map(Order::getUserId).toList();
        List<User> users = userMapper.selectBatchIds(userIds);

        for (Order order : orderPage.getRecords()) {
            StorageCabinet storageCabinet = storageCabinets.stream().filter(x -> x.getId() == order.getStorageCabinetId()).findFirst().orElse(null);
            if(storageCabinet == null) continue;

            User user = users.stream().filter(x -> x.getId() == order.getUserId()).findFirst().orElse(null);
            if(user == null) continue;

            OrderLostItemDto dto = new OrderLostItemDto();
            dto.setOrderId(order.getId());
            dto.setCabinetNumber(storageCabinet.getNum());
            dto.setName(order.getName());
            dto.setSizeType(storageCabinet.getSizeType());
            dto.setStorageStatus(order.getStorageStatus());
            dto.setStorageName(user.getAccountName());
            dto.setStorageTime(order.getStorageTime());
            calculateOrderEndTime(dto, order.getDateType());
            orderLostItemList.add(dto);
        }

        return new GetAllOrderLostItemResponse(orderLostItemList, orderPage.getTotal());
    }

    @Override
    public GetOrderStatisticalDataResponse GetOrderStatistical(GetOrderStatisticalDataRequest request) {
        var queryOrderWrapper = new QueryWrapper<Order>();

        queryOrderWrapper.between("storage_time", request.getStartTime(), request.getEndTime());

        queryOrderWrapper.select("DATE(storage_time) as day", "COUNT(*) as order_count")
                .groupBy("DATE(storage_time)");

        List<Map<String, Object>> result = orderMapper.selectMaps(queryOrderWrapper);

        List<OrderStatisticalDto> dataList = new ArrayList<>();

        for (Map<String, Object> map : result) {
            java.sql.Date daySqlDate = (java.sql.Date) map.get("day");

            LocalDate day = daySqlDate.toLocalDate();

            LocalDateTime startOfDay = day.atStartOfDay();

            Long orderCount = (Long) map.get("order_count");

            OrderStatisticalDto orderStatisticalData = new OrderStatisticalDto();
            orderStatisticalData.setDate(startOfDay);  // ���� LocalDateTime ���͵�����
            orderStatisticalData.setUsageCount(orderCount.intValue());  // �� Long ת��Ϊ int

            dataList.add(orderStatisticalData);  // �������ӵ������б�
        }

        return new GetOrderStatisticalDataResponse(dataList);
    }

    public void calculateOrderEndTime(OrderLostItemDto order, StorageDateType dateType) {
        LocalDateTime endTime = order.getStorageTime();

        endTime = switch (dateType) {
            case OneWeek -> endTime.plusWeeks(1);
            case OneMonth -> endTime.plusMonths(1);
            default -> throw new IllegalArgumentException("��֧�ֵ���������: " + dateType);
        };

        order.setEndTime(endTime);
    }

    // �����������ͻ�ȡԤ��ʱ�䣨�룩
    private long getEstimatedTime(StorageDateType dateType) {
        return switch (dateType) {
            case ThreeDays -> Duration.ofDays(3).toSeconds();
            case OneWeek -> Duration.ofDays(7).toSeconds();
            case OneMonth -> Duration.ofDays(30).toSeconds();
        };
    }

    // �����ѹ�����
    private long calculateDaysDifference(long pastTime, long estimatedTime) {
        long diff = pastTime - estimatedTime; // ��������
        return diff / (24 * 60 * 60); // ��������
    }

    // ��ȡ��Ӧ�����Ĺ�������
    private StorageCabinetSetting getCabinetSettingByDays(long diffInDays, List<StorageCabinetSetting> cabinetSettings) {
        if (diffInDays <= 3 && diffInDays > 0) {
            return cabinetSettings.stream().filter(x -> x.getDateType() == StorageDateType.ThreeDays).findFirst().orElse(null);
        } else if (diffInDays > 3 && diffInDays <= 7) {
            return cabinetSettings.stream().filter(x -> x.getDateType() == StorageDateType.OneWeek).findFirst().orElse(null);
        } else if (diffInDays > 7) {
            return cabinetSettings.stream().filter(x -> x.getDateType() == StorageDateType.OneMonth).findFirst().orElse(null);
        }
        return null;
    }

    // ���������������۸�
    private float calculatePriceBasedOnDiff(long diffInDays, StorageCabinetSetting cabinetSetting, float estimatedPrice) {
        if (diffInDays <= 3) {
            estimatedPrice += cabinetSetting.getPrice();
        } else if (diffInDays <= 7) {
            estimatedPrice += cabinetSetting.getPrice();
        } else {
            long monthCount = diffInDays / 30;  // ʹ��30��Ϊһ����
            estimatedPrice += monthCount * cabinetSetting.getPrice();
        }
        return estimatedPrice;
    }


    private float CalculateOrderEstimatedPrice(int monthCount, float settingPrice, StorageDateType dateType) {
        if(dateType != StorageDateType.OneMonth)return settingPrice;

        return monthCount * settingPrice;
    }

    private StorageCabinet DistributeVacantStorageCabinet(StorageCabinetSizeType sizeType) {
        var queryWrapper = new QueryWrapper<StorageCabinet>();

        queryWrapper.eq("is_stored", false).eq("size_type", sizeType).last("limit 1");

        return storageCabinetMapper.selectOne(queryWrapper);
    }

    private UserVoucherNumber GenerateUserVoucherNumber(long userId) {
        UserVoucherNumber userVoucherNumber = new UserVoucherNumber();
        userVoucherNumber.setUserId(userId);
        userVoucherNumber.setVoucherNumber(uniqueNumberService.generateUniqueNumber());
        userVoucherNumber.setCreatedDate(LocalDateTime.now());

        return userVoucherNumber;
    }
}
