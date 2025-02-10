package org.code.airportitemstorage.library.dto.order;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderStatisticalDto {
    public LocalDateTime date;
    public long usageCount;
}
