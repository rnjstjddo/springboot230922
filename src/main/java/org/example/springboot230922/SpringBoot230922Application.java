package org.example.springboot230922;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringBoot230922Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringBoot230922Application.class, args);
        System.out.println("메인 Application 진입");
    }
}
