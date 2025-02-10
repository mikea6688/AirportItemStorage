package org.code.airportitemstorage.service.order;

import org.code.airportitemstorage.library.entity.orders.Order;
import org.code.airportitemstorage.library.entity.storageCabinet.StorageCabinetSetting;
import org.code.airportitemstorage.library.request.order.*;

import java.util.List;

public interface OrderService  {
    int AddUserOrder(AddOrderRequest request) throws Exception;

    GetUserOrderResponse GetUserOrderList(GetUserOrderRequest request);

    boolean PayUserOrder(PayUserOrderRequest request) throws Exception;

    float CalculateTotalPrice(Order order, long storageDuration, List<StorageCabinetSetting> settings);

    GetOrderLogisticsInfoResponse GetOrderLogisticsInfo(GetOrderLogisticsInfoRequest request);

    GetAllOrderResponse GetAllOrder(GetAllOrderRequest request);

    GetAllOrderLogisticsListResponse GetAllOrderLogisticsList(GetAllOrderLogisticsListRequest request);

    GetAllOrderLostItemResponse GetAllOrderLostItemList(GetAllOrderLostItemRequest request);

    GetOrderStatisticalDataResponse GetOrderStatistical(GetOrderStatisticalDataRequest request);

    int DeleteOrderLostItem(long id) throws Exception;

    int OperateLogisticsOrder(OperateLogisticsOrderRequest request) throws Exception;
}
