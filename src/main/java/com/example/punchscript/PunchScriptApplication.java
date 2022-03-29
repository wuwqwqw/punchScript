package com.example.punchscript;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class PunchScriptApplication {

    public static void main(String[] args) {
        SpringApplication.run(PunchScriptApplication.class, args);
    }

}
