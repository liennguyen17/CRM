package com.example.democrm.service;

import com.example.democrm.etity.User;
import com.example.democrm.model.UserDetailsImpl;
import com.example.democrm.repository.RoleRepository;
import com.example.democrm.repository.UserRepository;
import com.example.democrm.response.LoginResponse;
import jakarta.validation.constraints.Email;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

public class AuthenServiceImpl implements AuthenService{
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenServiceImpl(AuthenticationManager authenticationManager,
                             UserRepository userRepository,
                             RoleRepository roleRepository,
                             PasswordEncoder passwordEncoder,
                             JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public LoginResponse authenticateUser(String userName, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userName, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        //other option generate jwt token with role_tao ma thong bao voi vai tro
        String jwt = jwtTokenProvider.generateTokenWithAuthorities(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new LoginResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles);

    }

    @Override
    public void registerUser(String userName, String password, String email) {
        if(userRepository.existsAllByUserName(userName)){
            throw new RuntimeException("Tài khoản đã tồn tại");
        }
        if(userRepository.existsAllByEmail(email)){
            throw new RuntimeException("Email đã tồn tại trong hệ thống!");
        }

        //// Create new user's account_ tạo tài khoản người dùng mới
        User user = User.builder()
                .userName(userName)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();

        userRepository.save(user);
    }
}
