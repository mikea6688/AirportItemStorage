package org.code.airportitemstorage.library.entity.orders;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.code.airportitemstorage.library.OrderLogisticsStatus;
import org.code.airportitemstorage.library.PaymentMethod;

/**
 * ¶©µ¥ÎïÁ÷
 */
@Data
public class OrderLogistics {
    @TableId
    private long id;

    @TableField("order_id")
    private long orderId;

    @TableField("user_id")
    private long userId;

    @TableField("recipient")
    private String recipient;

    @TableField("phone")
    private String phone;

    @TableField("delivery_address")
    private String deliveryAddress;

    @TableField("status")
    private OrderLogisticsStatus status;

    @TableField("payment_method")
    private PaymentMethod paymentMethod;
}
