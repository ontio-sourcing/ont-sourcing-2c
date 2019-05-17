package com.ontology.sourcing2c;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.ontology.sourcing2c.mapper")
public class OntSourcingApplication {

    public static void main(String[] args) {
        SpringApplication.run(OntSourcingApplication.class, args);
    }

}
