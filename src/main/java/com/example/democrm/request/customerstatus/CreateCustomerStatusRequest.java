package com.example.democrm.request.customerstatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCustomerStatusRequest {
    @NotBlank(message = "Tên trạng thái không được để trống!")
    @Size(min = 5, max = 100, message = "Tên trạng thái phải có ít nhất 5, nhiều nhất 100 kí tự!")
//    @Pattern(regexp = "^([\\p{L}\\d_]+)$", message = "Tên trạng thái không được chứa ký tự đặc biệt!")
    private String statusName;
}
