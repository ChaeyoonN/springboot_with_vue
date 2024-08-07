package com.example.demo.service;

import java.util.Collections;

import javax.swing.Spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Users;
import com.example.demo.repository.UsersRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  // UserDetailsService 란? Spring Security에서 유저의 정보를 가져오는 인터페이스
 
  private final UsersRepository usersRepository;

  public CustomUserDetailsService(UsersRepository usersRepository) {
      this.usersRepository = usersRepository;
  }

  // 기본 오버라이드 메서드 loadUserByUsername
  // 인자로 유저의 정보를 불러와서 DB를 조회 후 저장되어 있는 UserDetails로 리턴 (로그인에 사용!)
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      Users user = usersRepository.findByUsername(username)
          .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

      return new org.springframework.security.core.userdetails.User(
          user.getUsername(), 
          user.getPassword(), 
          user.getAuthorities()
      );
  }
}
