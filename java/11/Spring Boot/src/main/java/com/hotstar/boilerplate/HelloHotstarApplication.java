package com.hotstar.boilerplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
@SpringBootApplication
public class HelloHotstarApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelloHotstarApplication.class, args);
    }

     @GetMapping("/healthy")
    public ResponseEntity<Map<String, String>> healthCheck() {
         Map<String, String> response = new HashMap<>();
         response.put("status", "Welcome to hello! hotstar bootcamp");
         return ResponseEntity.ok().body(response);
    }


}


