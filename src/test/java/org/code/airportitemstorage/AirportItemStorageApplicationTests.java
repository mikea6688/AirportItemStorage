package org.code.airportitemstorage;

import org.junit.jupiter.api.Test;
import org.code.airportitemstorage.service.UniqueNumberService;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AirportItemStorageApplicationTests {
	@Test
	void contextLoads() {
		UniqueNumberService uniqueNumberService = new UniqueNumberService();
		String l = uniqueNumberService.generateUniqueNumber();

		System.out.println(l);
	}

}
