package com.ndashimye.firstapp;

import com.ndashimye.firstapp.model.User;
import com.ndashimye.firstapp.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
public class FirstAppApplication {

    public static void main(String[] args) {
		SpringApplication.run(FirstAppApplication.class, args);
	}

}
