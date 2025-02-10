package org.code.airportitemstorage.mapper.notification;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.code.airportitemstorage.library.entity.notification.Notification;

@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {
}
