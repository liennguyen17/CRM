package com.example.democrm.controller;

import com.example.democrm.constant.ErrorCodeDefs;
import com.example.democrm.request.auth.LoginRequest;
import com.example.democrm.request.auth.RegisterRequest;
import com.example.democrm.response.BaseItemResponse;
import com.example.democrm.response.BaseResponse;
import com.example.democrm.response.LoginResponse;
import com.example.democrm.service.AuthenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController extends BaseController {
    private AuthenService authenService;

    @Autowired
    public AuthController(AuthenService authenService) {
        this.authenService = authenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse jwtResponse = authenService.authenticateUser(loginRequest.getUserName(), loginRequest.getPassword());
//        return buildItemResponse(jwtResponse);
        BaseItemResponse<LoginResponse> response = new BaseItemResponse<>();
        response.setData(jwtResponse);
        response.setSuccess(true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest signUpRequest) {
        BaseResponse response = new BaseResponse();
        try {
            authenService.registerUser(signUpRequest.getUserName(),
                    signUpRequest.getPassword(),
                    signUpRequest.getEmail(),
                    signUpRequest.getIsSuperAdmin());
            response.setSuccess(true);
        } catch (Exception ex) {
            response.setFailed(ErrorCodeDefs.SERVER_ERROR, ex.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}
