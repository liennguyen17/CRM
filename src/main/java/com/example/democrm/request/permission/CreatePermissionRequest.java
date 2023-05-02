package com.example.democrm.request.permission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreatePermissionRequest {
    @NotBlank(message = "Id không được để trống")
    private String permissionId;
    @NotBlank(message = "Tên quyền không được để trống")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Tên quyền không được chứa ký tự đặc biệt!")
    private String permissionName;
    @NotBlank(message = "Mô tả quyền không được để trống")
    private String description;
    @NotNull(message = "Trạng thái quyền không được để trống nhập(1: Hoạt động, 0: Không hoạt động)")
    private Boolean status;
}
