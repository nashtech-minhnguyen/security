package com.softbank.security.config;

import com.softbank.security.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtProvider jwtProvider;

  private final AccountService accountService;

  private JwtAuthenticationFilter jwtAuthenticationFilter(){
    return new JwtAuthenticationFilter(jwtProvider, accountService);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity.authorizeRequests(authorize -> {
      try {
        authorize
            .antMatchers("/hello").permitAll()
            .antMatchers("/accounts/**").permitAll()
            .antMatchers("/login").permitAll()
            .antMatchers("/user").hasAuthority("USER")
            .antMatchers("/admin").hasAuthority("ADMIN")
            .anyRequest().authenticated().and().addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
      } catch (Exception e) {
        throw new RuntimeException();
      }
    }).csrf().disable().build();
  }

//  @Bean
//  public UserDetailsManager userDetailsManager() {
//    UserBuilder users = User.builder();
//    UserDetails user = users
//        .authorities("USER")
//        .username("John")
//        .password("{bcrypt}$2a$10$lyhnoFYuWe0yN1Eqx6bPTOhx5F5B1zu5.5lm9bBEwyGTdqw3DpiD.")
//        .build();
//    UserDetails admin = users
//        .authorities("ADMIN")
//        .username("Nick")
//        .password("{bcypt}$2a$10$p7Bd7xBnjf5ekM3KGCvVieEZBKDUkFsLJWijthcsQvQZJvFSnJ/tO")
//        .build();
//    return new InMemoryUserDetailsManager(user, admin);
//  }

//  @Bean
//  public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
//    JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
//    String accountCodeQuery = "SELECT account_code, concat('{bcrypt}',password),"
//        + " CASE WHEN status = 'ACTIVE' THEN true ELSE false END AS status"
//        + " FROM accounts WHERE account_code = ? ";
//    String accountRoleQuery = "SELECT account_code, role FROM accounts WHERE account_code = ?";
//    userDetailsManager.setUsersByUsernameQuery(accountCodeQuery);
//    userDetailsManager.setAuthoritiesByUsernameQuery(accountRoleQuery);
//    return userDetailsManager;
//  }

}

