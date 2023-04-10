package com.example.democrm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role1DTO {
    private Long roleId;
    private String roleName;
    private Boolean status;
}
