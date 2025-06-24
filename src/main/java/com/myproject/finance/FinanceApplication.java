package com.myproject.finance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FinanceApplication {
        public static void main(String[] args) {
            System.out.println("********************************");
            System.out.println("*   Personal Finance Manager   *");
            System.out.println("********************************");
            SpringApplication.run(FinanceApplication.class, args);
    }
}