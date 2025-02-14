package org.code.airportitemstorage.library.entity.storageCabinet;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.code.airportitemstorage.library.StorageCabinetSizeType;

import java.time.LocalDateTime;

@Data
public class StorageCabinet {
    @TableId
    private long id;

    @TableField("num")
    private String num;

    @TableField("name")
    private String name;

    @TableField("size_type")
    private StorageCabinetSizeType sizeType;

    @TableField("is_stored")
    private boolean isStored;

    @TableField("is_required_clean")
    private boolean isRequiredClean;

    @TableField("created_date")
    private LocalDateTime createdDate;
}
