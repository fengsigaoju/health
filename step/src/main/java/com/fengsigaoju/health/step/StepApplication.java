package com.fengsigaoju.health.step;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableEurekaClient
@EnableRedisRepositories
@MapperScan("com.fengsigaoju.health.step.dao")
public class StepApplication {

	public static void main(String[] args) {
		SpringApplication.run(StepApplication.class, args);
	}
}
