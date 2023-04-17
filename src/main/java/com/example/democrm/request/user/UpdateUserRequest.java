package com.example.democrm.request.user;

import com.example.democrm.validator.DateValidateAnnotation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @NotNull(message = "Id không được để trống")
    private Long id;
    @NotBlank(message = "Tên nguời dùng không được để trống")
    @Size(min = 6, max = 100, message = "Tên phải có ít nhất 6, nhiều nhất 100 kí tự!")
    private String userName;
    @NotBlank(message = "Ngày sinh không được để trống")
    @DateValidateAnnotation(message = "Định dạng ngày tháng phải là dd/MM/YYYY")
    private String date;
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ!")
    private String email;
    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;
    @NotNull(message = "Nhập người dùng có chỉ định là Admin hay không(1: có, 0: không)")
    private Boolean isSuperAdmin;
    //    @NotNull(message = "Role_id không được để trống")
    private Long roleId;
}
