package com.example.democrm.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Tên đăng ký không được để trống!")
    @Size(min = 6, max = 100, message = "Tên đăng ký phải có ít nhất 6, nhiều nhất 100 kí tự!")
    private String userName;
    @NotBlank(message = "Mật khẩu không được để trống!")
    @Size(min = 6, max = 20, message = "Mật khẩu phải có ít nhất 6, nhiều nhất 20 kí tự!")
    private String password;
    @NotBlank(message = "Email không được để trống!")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email không hợp lệ!")
    private String email;
    private Boolean isSuperAdmin = false;


}
