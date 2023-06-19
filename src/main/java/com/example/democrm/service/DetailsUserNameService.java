package com.example.democrm.service;

import com.example.democrm.dto.UserDTO;
import com.example.democrm.etity.User;
import com.example.democrm.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
@Service
public class DetailsUserNameService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    @Value("${jwt.jwtSecret}")
    private String jwtSecret;
    @Autowired
    public DetailsUserNameService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO getUserFromToken(String token) {
        // Giải mã token để lấy thông tin người dùng
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret) // Key bí mật sử dụng để giải mã token
                .parseClaimsJws(token)
                .getBody();

        Long userId = Long.parseLong(claims.getSubject());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return userDTO;
    }
}
