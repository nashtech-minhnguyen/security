package com.softbank.security.service;

import com.softbank.security.custom.CustomUserDetails;
import com.softbank.security.dto.AccountDto;
import com.softbank.security.entity.Account;
import org.springframework.security.core.userdetails.UserDetails;;

public interface AccountService {

  void createAccount(AccountDto account);

  void disableAccount(String accountCode);

  boolean isCorrectPasswordForAccount(String accountCode, String password);

  CustomUserDetails customAccount(String accountCode, String password);

  CustomUserDetails getAccountByAccountCode(String accountCode);
}
