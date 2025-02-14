package org.code.airportitemstorage.mapper.order;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.code.airportitemstorage.library.entity.orders.OrderExpiredNotifyRecord;

@Mapper
public interface OrderExpiredNotifyRecordMapper extends BaseMapper<OrderExpiredNotifyRecord> {
}
