package com.example.democrm.dto;

import com.example.democrm.constant.DateTimeConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomersDTO {
    private Long customerId;
    private String customerName;
    @JsonFormat(pattern = DateTimeConstant.DATE_TIME_FORMAT, timezone = DateTimeConstant.TIME_ZONE)
    private Timestamp createdDate;
    @JsonFormat(pattern = DateTimeConstant.DATE_TIME_FORMAT, timezone = DateTimeConstant.TIME_ZONE)
    private Timestamp updateDate;
    private String phone;
    private String email;
    private String note;
    private String address;
    private User1DTO user;
    private CustomerStatusDTO status;
    private CustomerGroupDTO group;


}
