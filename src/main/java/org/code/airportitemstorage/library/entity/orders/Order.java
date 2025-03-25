package org.code.airportitemstorage.library.entity.orders;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.code.airportitemstorage.library.OrderStorageStatus;
import org.code.airportitemstorage.library.StorageDateType;

import java.time.LocalDateTime;

/**
 * 用户订单
 */
@Data
@TableName("`order`")
public class Order {
    @TableId
    private long id;

    @TableField("user_id")
    private long userId;

    @TableField("storage_cabinet_id")
    private long storageCabinetId;

    @TableField("name")
    private String name;

    @TableField("voucher_number")
    private String voucherNumber;

    @TableField("is_valuable")
    private boolean isValuable;

    @TableField("use_member_renewal_service")
    private boolean useMemberRenewalService;

    @TableField("is_lost_item")
    private boolean isLostItem;

    @TableField("price")
    private float price;

    @TableField("estimated_price")
    private float estimatedPrice;

    @TableField("storage_status")
    private OrderStorageStatus storageStatus;

    @TableField("month_count")
    private int monthCount;

    @TableField("date_type")
    private StorageDateType dateType;

    @TableField("storage_time")
    private LocalDateTime storageTime;

    @TableField("total_stored_duration")
    private long totalStoredDuration;

    @TableField("category_id")
    private long categoryId;
}
