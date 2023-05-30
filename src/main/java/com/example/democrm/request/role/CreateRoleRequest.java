package com.example.democrm.request.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateRoleRequest {
    @NotBlank(message = "Tên vai trò không được để trống!")
    @Size(min = 5, max = 100, message = "Tên vai trò phải có ít nhất 5, nhiều nhất 100 kí tự!")
//    @Pattern(regexp = "^([\\p{L}\\d_]+)$", message = "Tên vai trò không được chứa ký tự đặc biệt!")
    private String roleName;
    @NotNull(message = "Trạng thái vai trò không được để trống nhập(1: Hoạt động, 0: Không hoạt động)")
    private Boolean status;
    @NotBlank(message = "Mô tả vai trò không được để trống!")
    private String descriptionRole;
    private List<String> permissionIds;
}
