package org.code.airportitemstorage.library.request.order;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetOrderStatisticalDataRequest {
    public LocalDateTime StartTime;
    public LocalDateTime EndTime;
}
