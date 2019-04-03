package com.tss.orderService;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.tss.orderService.mapper")
public class ThirdServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ThirdServiceApplication.class, args);
	}

}
