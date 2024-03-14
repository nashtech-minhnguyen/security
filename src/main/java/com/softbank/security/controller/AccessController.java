package com.softbank.security.controller;

import com.softbank.security.config.JwtProvider;
import com.softbank.security.custom.CustomUserDetails;
import com.softbank.security.dto.LoginInfoDto;
import com.softbank.security.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class AccessController {

  @Autowired
  private AccountService accountService;

 @Autowired
  private JwtProvider jwtConfig;

  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody LoginInfoDto loginInfo) {
    CustomUserDetails customedAccount = accountService.customAccount(loginInfo.getAccountCode(), loginInfo.getPassword());
    if (Objects.nonNull(customedAccount)) {
      return ResponseEntity.status(HttpStatus.ACCEPTED).body(jwtConfig.generateToken(customedAccount));
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }
}
