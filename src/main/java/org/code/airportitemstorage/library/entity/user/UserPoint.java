package org.code.airportitemstorage.library.entity.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_point")
public class UserPoint {
    @TableId
    private long id;

    @TableField("user_id")
    private long userId;

    private double point;

//    @TableField("create_date")
    private LocalDateTime createDate;
}
