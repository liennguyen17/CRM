package com.example.democrm.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String jwt;
    private Long id;
    private String userName;
    private String email;
    private List<String> roles;
}
