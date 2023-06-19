package com.example.democrm.controller;

import com.example.democrm.dto.UserDTO;
import com.example.democrm.service.DetailsUserNameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class DetaiUserNameController {
    private final DetailsUserNameService detailsUserNameService;

    public DetaiUserNameController(DetailsUserNameService detailsUserNameService) {
        this.detailsUserNameService = detailsUserNameService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getUserProfile(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7); // Bỏ qua tiền tố "Bearer "
        UserDTO userDTO = detailsUserNameService.getUserFromToken(jwtToken);
        return ResponseEntity.ok(userDTO);
    }
}
