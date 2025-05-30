package org.code.airportitemstorage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@MapperScan("org.code.airportitemstorage.mapper")
public class AirportItemStorageApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirportItemStorageApplication.class, args);
	}

}
