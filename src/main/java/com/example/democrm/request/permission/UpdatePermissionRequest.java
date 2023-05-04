package com.example.democrm.request.permission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePermissionRequest {
    @NotBlank(message = "Tên quyền không được để trống!")
    @Size(min = 5,max = 100, message = "Tên quyền phải có ít nhất 5, nhiều nhất 100 kí tự!")
    @Pattern(regexp = "^([\\p{L}\\d_]+)$", message = "Tên quyền không được chứa ký tự đặc biệt!")
    private String permissionName;
    @NotBlank(message = "Mô tả quyền không được để trống!")
    private String description;
    @NotNull(message = "Trạng thái quyền không được để trống nhập(1: Hoạt động, 0: Không hoạt động)")
    private Boolean status;
}
