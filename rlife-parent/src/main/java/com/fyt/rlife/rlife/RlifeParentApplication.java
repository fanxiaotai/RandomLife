package com.fyt.rlife.rlife;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.fyt.rlife.rlife.mapper")
public class RlifeParentApplication {

    public static void main(String[] args) {
        SpringApplication.run(RlifeParentApplication.class, args);
    }

}
