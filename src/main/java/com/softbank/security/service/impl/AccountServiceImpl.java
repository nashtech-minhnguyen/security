package com.softbank.security.service.impl;

import com.softbank.security.custom.CustomUserDetails;
import com.softbank.security.dto.AccountDto;
import com.softbank.security.entity.Account;
import com.softbank.security.enums.Role;
import com.softbank.security.enums.Status;
import com.softbank.security.repository.AccountRepository;
import com.softbank.security.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

  private static final int ACCOUNT_CODE_LENGTH = 9;

  private static final int LETTER_ACCOUNT_CODE_LENGTH = 2;

  private static final int NUMBER_OF_DIGITS = 10;

  private static final int NUMBER_OF_LETTERS = 26;

  private static final int PASSWORD_LENGTH = 6;

  private static final Random RANDOM = new Random();

  private final AccountRepository accountRepository;

  @Override
  @Transactional
  public void createAccount(AccountDto accountDto) {
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    String password = bCryptPasswordEncoder.encode(accountDto.getPassword());
    String accountCode = generateAccountCode();

    Account account = new Account();
    account.setAccountCode(accountCode);
    account.setPassword(password);
    account.setRole(accountDto.getRole());
    account.setStatus(Status.ACTIVE);

    accountRepository.save(account);
  }

  @Override
  @Transactional
  public void disableAccount(String accountCode) {
    Account account = accountRepository.findByAccountCode(accountCode).orElseThrow();
    account.setStatus(Status.INACTIVE);
    accountRepository.save(account);
  }

  @Override
  public boolean isCorrectPasswordForAccount(String accountCode, String password) {
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    Account account = accountRepository.findByAccountCode(accountCode).orElseThrow();
    return bCryptPasswordEncoder.matches(password, account.getPassword());
  }

  @Override
  public CustomUserDetails customAccount(String accountCode, String password) {
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    Account account = accountRepository.findByAccountCode(accountCode).orElseThrow();
    boolean isExistedAccount = bCryptPasswordEncoder.matches(password, account.getPassword());
    if (isExistedAccount) {
      return new CustomUserDetails(account);
    }
    return null;
  }

  @Override
  public CustomUserDetails getAccountByAccountCode(String accountCode) {
    Account account = accountRepository.findByAccountCode(accountCode).orElseThrow();
    return new CustomUserDetails(account);
  }

  private String generateAccountCode() {
    StringBuilder accountCode = new StringBuilder();
    for (int i = 0; i < ACCOUNT_CODE_LENGTH; i++) {
      if (i < LETTER_ACCOUNT_CODE_LENGTH) {
        accountCode.append((char) ('A' + RANDOM.nextInt(NUMBER_OF_LETTERS)));
      } else {
        accountCode.append(RANDOM.nextInt(NUMBER_OF_DIGITS));
      }
    }
    return accountCode.toString();
  }
}
