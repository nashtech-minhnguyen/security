package com.softbank.security.controller;

import com.softbank.security.dto.AccountDto;
import com.softbank.security.enums.Role;
import com.softbank.security.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController {

  @Autowired
  private AccountService accountService;

  @PostMapping
  public ResponseEntity<Void> createAccount(@RequestBody AccountDto account) {
    accountService.createAccount(account);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping
  public ResponseEntity<Void> disableAccount(@RequestParam String accountCode) {
    accountService.disableAccount(accountCode);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{accountCode}/password/{password}")
  public ResponseEntity<String> isCorrectPasswordForAccount(@PathVariable String accountCode, @PathVariable String password) {
    if (accountService.isCorrectPasswordForAccount(accountCode, password)) {
      return ResponseEntity.ok("Correct");
    }
    return ResponseEntity.ok("Incorrect");
  }
}
