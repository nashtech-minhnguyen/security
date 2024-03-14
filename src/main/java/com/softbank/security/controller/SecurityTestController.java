package com.softbank.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityTestController {

  @GetMapping("/hello")
  public ResponseEntity<String> sayHello() {
    return ResponseEntity.ok("Hello World");
  }

  @GetMapping("/admin")
  public ResponseEntity<String> sayHelloAdmin() {
    return ResponseEntity.ok("Hello Admin");
  }

  @GetMapping("/user")
  public ResponseEntity<String> sayHelloUser() {
    return ResponseEntity.ok("Hello User");
  }

}
