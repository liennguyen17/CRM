package com.example.democrm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User1DTO {
    private Long userId;
    private String userName;
//    private Role1DTO role;

}
