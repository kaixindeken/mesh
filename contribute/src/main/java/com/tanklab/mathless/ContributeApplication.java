package com.tanklab.mathless;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.tanklab.mathless.mapper")
@EnableFeignClients(basePackages = {"com.tanklab.mathless"})
public class ContributeApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContributeApplication.class, args);
    }
}
