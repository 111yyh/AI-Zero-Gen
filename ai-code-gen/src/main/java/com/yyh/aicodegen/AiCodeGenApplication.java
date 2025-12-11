package com.yyh.aicodegen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yyh.aicodegen.mapper")
public class AiCodeGenApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiCodeGenApplication.class, args);
    }

}
