package com.cheer.hole;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@MapperScan(basePackages = "com.cheer.hole.dao")
//@Cacheable
public class HoleApplication {

    public static void main(String[] args) {
        SpringApplication.run(HoleApplication.class, args);
    }

}
