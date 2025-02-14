package org.code.airportitemstorage.library.entity.storageCabinet;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.code.airportitemstorage.library.StorageCabinetSizeType;
import org.code.airportitemstorage.library.StorageDateType;

@Data
public class StorageCabinetSetting {
    @TableId
    private long id;

    @TableField("size_type")
    private StorageCabinetSizeType sizeType;

    @TableField("height")
    private String height;

    @TableField("width")
    private String width;

    @TableField("length")
    private String length;

    @TableField("date_type")
    private StorageDateType dateType;

    @TableField("price")
    private float price;
}
