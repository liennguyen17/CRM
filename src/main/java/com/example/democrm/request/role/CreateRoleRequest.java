package com.example.democrm.request.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateRoleRequest {
    @NotBlank(message = "Tên vai trò không được để trống")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Tên vai trò không được chứa ký tự đặc biệt!")
    private String roleName;
    @NotNull(message = "Trạng thái vai trò không được để trống nhập(1: Hoạt động, 0: Không hoạt động)")
    private Boolean status;

    private List<String> permissionIds;
}
