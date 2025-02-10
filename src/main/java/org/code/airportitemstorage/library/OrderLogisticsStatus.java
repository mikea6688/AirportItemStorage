package org.code.airportitemstorage.library;

import lombok.Getter;

@Getter
public enum OrderLogisticsStatus {
    Pending(1),
    InTransit(2),
    Arrived(3),
    Discarded(4);

    private final int order;

    OrderLogisticsStatus(int order) {
        this.order = order;
    }
}
