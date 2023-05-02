package com.example.democrm.request.customergroup;

import com.example.democrm.validator.DateValidateAnnotation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCustomerGroupRequest {
    @NotBlank(message = "Tên nhóm khách hàng không được để trống")
    @Size(min = 6, max = 100, message = "Tên phải có ít nhất 6, nhiều nhất 100 kí tự!")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Tên nhóm khách hàng không được chứa ký tự đặc biệt!")
    private String groupName;
    @NotNull(message = "User_id không được để trống")
    private Long userId;

}
