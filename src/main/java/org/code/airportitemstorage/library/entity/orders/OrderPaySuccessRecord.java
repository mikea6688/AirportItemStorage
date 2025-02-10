package org.code.airportitemstorage.library.entity.orders;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class OrderPaySuccessRecord {
    @TableId
    private long id;

    @TableField("order_id")
    private long orderId;

    @TableField("user_id")
    private long userId;

    public OrderPaySuccessRecord(long orderId, long userId) {
        this.orderId = orderId;
        this.userId = userId;
    }
}
