package org.code.airportitemstorage.library.entity.storageCabinet;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 用户存储凭证号码
 */
@Data
public class UserVoucherNumber {
    @TableId
    private long id;

    @TableField("user_id")
    private long userId;

    @TableField("voucher_number")
    private String voucherNumber;

    @TableField("created_date")
    private LocalDateTime createdDate;
}
