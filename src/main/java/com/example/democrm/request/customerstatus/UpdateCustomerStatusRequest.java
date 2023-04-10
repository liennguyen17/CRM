package com.example.democrm.request.customerstatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCustomerStatusRequest {
//    @NotNull(message = "Id không được để trống")
//    private Long statusId;

    @Size(min = 5, message = "statusName tối thiểu 5 kí tự")
    @NotBlank(message = "statusName không được để trống")
    private String statusName;
}
