package com.example.democrm.controller;

import com.example.democrm.response.BaseItemResponse;
import com.example.democrm.response.TestResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/all")
    public BaseItemResponse allAccess() {
        TestResponse response = new TestResponse("Public Content.");
        return new BaseItemResponse<>(true, null, response);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public BaseItemResponse adminAccess() {
        TestResponse response = new TestResponse("Admin Board.");
        return new BaseItemResponse<>(true, null, response);
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyAuthority('STAFF')")
    public BaseItemResponse userAccess() {
        TestResponse response = new TestResponse("Staff Board.");
        return new BaseItemResponse<>(true, null, response);
    }
}
