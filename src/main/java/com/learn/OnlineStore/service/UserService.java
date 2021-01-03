package com.learn.OnlineStore.service;

import com.learn.OnlineStore.dto.UserDto;
import com.learn.OnlineStore.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;

public interface UserService extends UserDetailsService {
    User save(UserDto userDto, HttpServletRequest request);
    User findById(Long id);
    User helpSave(User user);
    void sendEmail(User user,HttpServletRequest request);
}
