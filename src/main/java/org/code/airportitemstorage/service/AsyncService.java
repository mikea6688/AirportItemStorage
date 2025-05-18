package org.code.airportitemstorage.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AsyncService {
    @Async
    public CompletableFuture<String> doTask(int i) {
        try {
            Thread.sleep(100); // 每个任务耗时100ms
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return CompletableFuture.completedFuture("任务" + i + " 完成");
    }

    public String doTaskSync(int i) {
        try {
            Thread.sleep(100); // 模拟耗时
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "同步任务" + i + " 完成";
    }
}
