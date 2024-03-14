package com.softbank.security.dto;

import com.softbank.security.enums.Role;
import lombok.Data;

@Data
public class AccountDto {

  private String password;

  private Role role;
}
