package org.code.airportitemstorage;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.code.airportitemstorage.controller.NotificationController;
import org.code.airportitemstorage.library.entity.notification.Notification;
import org.code.airportitemstorage.library.request.notification.AddNotificationRequest;
import org.code.airportitemstorage.mapper.notification.NotificationMapper;
import org.code.airportitemstorage.service.AsyncService;
import org.code.airportitemstorage.service.notification.NotificationService;
import org.junit.jupiter.api.Test;
import org.code.airportitemstorage.service.UniqueNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AirportItemStorageApplicationTests {
	@Autowired
	private AsyncService asyncService;
	@Autowired
	private NotificationController notificationController;
	@Autowired
	private NotificationMapper notificationMapper;

	@Test
	void contextLoads() {
		UniqueNumberService uniqueNumberService = new UniqueNumberService();
		String l = uniqueNumberService.generateUniqueNumber();

		System.out.println(l);
	}

	@Test
	public void testAsyncConcurrency_100Tasks() throws Exception {
		long syncStart = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			String result = asyncService.doTaskSync(i);
			if (i < 3) System.out.println(result); // 打印前3个结果示例
		}
		long syncDuration = System.currentTimeMillis() - syncStart;
		System.out.println("同步执行100次总耗时: " + syncDuration + "ms");

		// 异步执行100次，统计耗时
		long asyncStart = System.currentTimeMillis();
		List<CompletableFuture<String>> futures = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			futures.add(asyncService.doTask(i));
		}
		CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

		for (int i = 0; i < 3; i++) { // 打印前3个结果示例
			System.out.println(futures.get(i).get());
		}

		long asyncDuration = System.currentTimeMillis() - asyncStart;
		System.out.println("异步执行100次总耗时: " + asyncDuration + "ms");

		assert asyncDuration < syncDuration;
	}

	@Test
	public void concurrentAddNotificationTest() {
		int threadCount = 100;
		List<Future<Integer>> futures = new ArrayList<>();

		try (ExecutorService executor = Executors.newFixedThreadPool(threadCount)) {
			for (int i = 0; i < threadCount; i++) {
				final int idx = i;
				futures.add(executor.submit(() -> {
					AddNotificationRequest req = new AddNotificationRequest();
					req.setTitle("标题" + idx);
					req.setContent("内容" + idx);
					return notificationController.addNotification(req).get();
				}));
			}
			for (Future<Integer> future : futures) {
				future.get();
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}

		long queryCount = notificationMapper.selectCount(new QueryWrapper<Notification>().eq("author", "USER"));
		assertThat(queryCount).isEqualTo(threadCount);
		System.out.println("并发生成Notification数量："+queryCount);

		notificationMapper.delete(new QueryWrapper<Notification>().eq("author", "USER"));
	}
}
