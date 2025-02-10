package org.code.airportitemstorage.controller;

import org.code.airportitemstorage.library.request.order.*;
import org.code.airportitemstorage.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("add")
    public int addOrder(@RequestBody AddOrderRequest request) throws Exception {
        return orderService.AddUserOrder(request);
    }

    @GetMapping("listByUserId")
    public GetUserOrderResponse getOrder(GetUserOrderRequest request) throws Exception {
        return orderService.GetUserOrderList(request);
    }

    @GetMapping("all")
    public GetAllOrderResponse getAllOrder(GetAllOrderRequest request){
        return orderService.GetAllOrder(request);
    }

    @PostMapping("payment")
    public boolean payOrder(@RequestBody PayUserOrderRequest request) throws Exception {
        return orderService.PayUserOrder(request);
    }

    @GetMapping("logistics/list")
    public GetOrderLogisticsInfoResponse getOrderLogisticsByCurrentUserInfo(GetOrderLogisticsInfoRequest request){
        return orderService.GetOrderLogisticsInfo(request);
    }

    @GetMapping("logistics/all")
    public GetAllOrderLogisticsListResponse getAllOrderLogisticsList(GetAllOrderLogisticsListRequest request){
        return orderService.GetAllOrderLogisticsList(request);
    }

    @GetMapping("lost/all")
    public GetAllOrderLostItemResponse getAllOrderLostItemList(GetAllOrderLostItemRequest request){
        return orderService.GetAllOrderLostItemList(request);
    }

    @GetMapping("statistical")
    public GetOrderStatisticalDataResponse getOrderStatistical(GetOrderStatisticalDataRequest request){
        return orderService.GetOrderStatistical(request);
    }
}
