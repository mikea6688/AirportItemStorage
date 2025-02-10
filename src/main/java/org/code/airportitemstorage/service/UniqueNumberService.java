package org.code.airportitemstorage.service;

import org.springframework.stereotype.Service;

@Service
public class UniqueNumberService {
    public String generateUniqueNumber() {
        long timestamp = System.currentTimeMillis(); // 获取当前时间的时间戳
        return String.valueOf(timestamp);
    }
}
