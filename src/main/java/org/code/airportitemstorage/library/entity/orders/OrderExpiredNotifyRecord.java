package org.code.airportitemstorage.library.entity.orders;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderExpiredNotifyRecord {
    @TableId
    private long id;

    @TableField("order_id")
    private long orderId;

    @TableField("created_date")
    private LocalDateTime createdDate;
}
