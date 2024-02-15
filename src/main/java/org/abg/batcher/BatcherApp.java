package org.abg.batcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BatcherApp {

    public static void main(String[] args) {
        SpringApplication.run(BatcherApp.class, args);
        System.out.println("HI");
    }


}
