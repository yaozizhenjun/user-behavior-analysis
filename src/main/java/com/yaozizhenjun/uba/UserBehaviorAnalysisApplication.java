package com.yaozizhenjun.uba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class UserBehaviorAnalysisApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserBehaviorAnalysisApplication.class, args);
    }
}
