package com.database.doc.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class,
        FreeMarkerAutoConfiguration.class})
@ComponentScan({"com.database"})
public class DocApplication {

    /**
     * 项目启动方法
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        SpringApplication.run(DocApplication.class, args);
    }

}
