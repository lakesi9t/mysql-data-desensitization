package com.yqkj.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAsync(proxyTargetClass = true)
public class DataDesensitizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataDesensitizationApplication.class, args);
    }
}
