package com.nikguscode.SusuAPIs;

import com.nikguscode.SusuAPIs.model.repositories.variables.AuthenticationVariables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SusuAPI implements CommandLineRunner {

    @Autowired
    private AuthenticationVariables authenticationVariables;

    public static void main(String[] args) {
        SpringApplication.run(SusuAPI.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        authenticationVariables.getVariables();
    }
}
