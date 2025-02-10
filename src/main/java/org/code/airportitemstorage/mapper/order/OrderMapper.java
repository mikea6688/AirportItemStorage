package org.code.airportitemstorage.mapper.order;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.code.airportitemstorage.library.entity.orders.Order;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
