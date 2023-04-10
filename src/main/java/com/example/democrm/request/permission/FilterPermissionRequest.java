package com.example.democrm.request.permission;

import com.example.democrm.validator.DateValidateAnnotation;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FilterPermissionRequest {
    @NotNull(message = "Start không được để trống")
    private Integer start;
    @NotNull(message = "Limit không được để trống")
    private Integer limit;
    private String permissionName;
    private String description;
    private Boolean status;
}
