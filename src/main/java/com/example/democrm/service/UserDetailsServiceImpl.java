package com.example.democrm.service;

import com.example.democrm.etity.User;
import com.example.democrm.model.UserDetailsImpl;
import com.example.democrm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepository userRepository;
    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    //tim kiem thong tin nguoi dung theo ten dang nhap tra ve doi tuong UserDetails
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(userName).orElseThrow(() -> new RuntimeException("User not found"));
        return UserDetailsImpl.builder()
                .userName(user.getUserName())
                .id(user.getUserId())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .email(user.getEmail())
                .build();
    }
}
