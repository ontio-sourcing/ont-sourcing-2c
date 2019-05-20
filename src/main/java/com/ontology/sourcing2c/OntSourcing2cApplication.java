package com.ontology.sourcing2c;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan(basePackages = "com.ontology.sourcing2c.mapper")
@EnableScheduling
public class OntSourcing2cApplication {

    public static void main(String[] args) {
        SpringApplication.run(OntSourcing2cApplication.class, args);
    }

}
