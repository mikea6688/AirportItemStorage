package org.code.airportitemstorage.library.entity.storageCabinet;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StorageCategory {
    @TableId
    private long id;

    @TableField("category_name")
    private String categoryName;

    @TableField("created_date")
    private LocalDateTime createdDate;
}
