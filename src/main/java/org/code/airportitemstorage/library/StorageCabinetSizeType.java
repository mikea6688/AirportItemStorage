package org.code.airportitemstorage.library;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum StorageCabinetSizeType {
    Small(1),
    Medium(2),
    Large(3);

    private final int order;

    StorageCabinetSizeType(int order) {
        this.order = order;
    }
}

