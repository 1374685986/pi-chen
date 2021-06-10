package com.chen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;


@SpringBootApplication
public class PiChenApplication {
    public static void main(String[] args) {
        System.out.println(new Date());
        SpringApplication.run(PiChenApplication.class, args);
    }
}

