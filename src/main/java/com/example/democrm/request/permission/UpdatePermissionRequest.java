package com.example.democrm.request.permission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdatePermissionRequest {
    @NotBlank(message = "Tên quyền không được để trống")
    private String permissionName;
    @NotBlank(message = "Mô tả quyền không được để trống")
    private String description;
    @NotNull(message = "Trạng thái quyền không được để trống nhập(1: Hoạt động, 0: Không hoạt động)")
    private Boolean status;
}
