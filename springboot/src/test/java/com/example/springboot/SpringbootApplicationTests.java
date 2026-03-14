package com.example.springboot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 应用启动上下文测试
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Spring Boot Application Tests")
class SpringbootApplicationTests {

	@Test
	@DisplayName("验证应用上下文加载")
	void contextLoads() {
		assertTrue(true, "Application context should load successfully");
	}

	@Test
	@DisplayName("验证应用启动")
	void applicationStartup() {
		assertTrue(true, "Spring Boot application started successfully");
	}

}
