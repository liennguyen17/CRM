package com.example.democrm.request.customerstatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FilterCustomerStatusRequest {
    @NotNull(message = "Start không được để trống")
    private Integer start;
    @NotNull(message = "Limit không được để trống")
    private Integer limit;
    @NotNull(message = "Tên trạng thái không được để trống")
    private String statusName;
}
