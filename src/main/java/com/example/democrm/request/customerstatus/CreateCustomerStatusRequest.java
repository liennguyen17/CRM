package com.example.democrm.request.customerstatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCustomerStatusRequest {
    @NotBlank(message = "Tên trạng thái không được để trống")
    @Size(min = 5, message = "Tên trạng thái tối thiểu 5 kí tự")
    private String statusName;
}
