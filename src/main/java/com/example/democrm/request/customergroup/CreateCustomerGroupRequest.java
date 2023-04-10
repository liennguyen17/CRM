package com.example.democrm.request.customergroup;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCustomerGroupRequest {
    @NotBlank(message = "Tên nhóm khách hàng không được để trống")
    @Size(min = 6, max = 100, message = "Tên phải có ít nhất 6, nhiều nhất 100 kí tự!")
    private String groupName;
    @NotNull(message = "User_id không được để trống")
    private Long userId;
}
