package org.code.airportitemstorage.service;

import org.springframework.stereotype.Service;

@Service
public class UniqueNumberService {
    public String generateUniqueNumber() {
        long timestamp = System.currentTimeMillis();
        return String.valueOf(timestamp);
    }
}
