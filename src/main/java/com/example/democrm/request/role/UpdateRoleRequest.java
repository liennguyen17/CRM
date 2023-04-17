package com.example.democrm.request.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class UpdateRoleRequest {
    @NotNull(message = "id không được để trống")
    private Long roleId;
    @NotBlank(message = "Tên vai trò không được để trống")
    private String roleName;
    @NotNull(message = "Trạng thái vai trò không được để trống nhập(1: Hoạt động, 0: Không hoạt động)")
    private Boolean status;
    private List<String> permissionIds;
}
