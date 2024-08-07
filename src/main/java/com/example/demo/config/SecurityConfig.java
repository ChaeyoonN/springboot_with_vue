package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.service.CustomUserDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final CustomUserDetailsService userDetailsService;

  public SecurityConfig(@Lazy CustomUserDetailsService userDetailsService) {
      this.userDetailsService = userDetailsService;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http
          .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
          .authorizeHttpRequests(authorize -> authorize
              .requestMatchers("/api/admin/**").hasRole("ADMIN") // ADMIN 역할만 접근 허용
              .requestMatchers("/api/user/**").hasAnyRole("ADMIN", "USER") // ADMIN 및 USER 역할 접근 허용
              .requestMatchers("/api/public/**").permitAll() // 모든 사용자 접근 허용
              .anyRequest().permitAll() // 나머지 요청은 인증 필요하지 않고 접근허용
          )
          .formLogin(form -> form
              .loginPage("/api/auth/login") // 커스텀 로그인 페이지 설정
              .permitAll() // 모든 사용자 로그인 페이지 접근 허용
          )
          .logout(logout -> logout
              .permitAll() // 모든 사용자 로그아웃 허용
          );

      return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder(); // 비밀번호 인코더 설정
  }


}

