package org.code.airportitemstorage.controller;

import org.code.airportitemstorage.library.dto.order.RenewalUserOrderRequest;
import org.code.airportitemstorage.library.request.order.*;
import org.code.airportitemstorage.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
// http://localhost:8080/api/order/add
@RestController
@RequestMapping("api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

//    用户用于物品存储时，调用下单接口
    @PostMapping("add")
    public int addOrder(@RequestBody AddOrderRequest request) throws Exception {
        return orderService.AddUserOrder(request);
    }
//  获取用户所有订单（不包括物流订单）
    @GetMapping("listByUserId")
    public GetUserOrderResponse getOrder(GetUserOrderRequest request) throws Exception {
        return orderService.GetUserOrderList(request);
    }
//  后台管理员看到所有用户订单
    @GetMapping("all")
    public GetAllOrderResponse getAllOrder(GetAllOrderRequest request){
        return orderService.GetAllOrder(request);
    }
//  用户取物品支付接口
    @PostMapping("payment")
    public boolean payOrder(@RequestBody PayUserOrderRequest request) throws Exception {
        return orderService.PayUserOrder(request);
    }
//  获取当前用户的物流信息
    @GetMapping("logistics/list")
    public GetOrderLogisticsInfoResponse getOrderLogisticsByCurrentUserInfo(GetOrderLogisticsInfoRequest request){
        return orderService.GetOrderLogisticsInfo(request);
    }
//  管理员所有物流信息
    @GetMapping("logistics/all")
    public GetAllOrderLogisticsListResponse getAllOrderLogisticsList(GetAllOrderLogisticsListRequest request){
        return orderService.GetAllOrderLogisticsList(request);
    }
//  管理员操作所有物流订单
    @PostMapping("logistics/operate")
    public int operateLogisticsOrder(@RequestBody OperateLogisticsOrderRequest request) throws Exception {
        return orderService.OperateLogisticsOrder(request);
    }
//管理员获取所有遗失物品
    @GetMapping("lost/all")
    public GetAllOrderLostItemResponse getAllOrderLostItemList(GetAllOrderLostItemRequest request){
        return orderService.GetAllOrderLostItemList(request);
    }
//丢弃遗失物品
    @PostMapping("lost/delete")
    public int deleteOrderLostItem(@RequestBody long id) throws Exception {
        return orderService.DeleteOrderLostItem(id);
    }
//统计所有有效订单
    @GetMapping("statistical")
    public GetOrderStatisticalDataResponse getOrderStatistical(GetOrderStatisticalDataRequest request){
        return orderService.GetOrderStatistical(request);
    }
    // 续期
    @PostMapping("renewal")
    public int RenewalUserOrder(@RequestBody RenewalUserOrderRequest request) throws Exception {
        return orderService.RenewalUserOrder(request);
    }
}
