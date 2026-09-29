package com.complaintbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class Complaintbox1Application {

    public static void main(String[] args) {

        SpringApplication.run(Complaintbox1Application.class, args);
    }

}
