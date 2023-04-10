package com.example.democrm.request.customers;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCustomerRequest {
    //    @NotNull(message = "Id không được để trống")
//    private Long customerId;
    @NotBlank(message = "Tên khách hàng không được để trống")
    @Size(min = 6, max = 100, message = "Tên phải có ít nhất 6, nhiều nhất 100 kí tự!")
    private String customerName;
    @NotBlank(message = "Số điện thoại không được để trống")
    private String phone;
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ!")
    private String email;
    @NotBlank(message = "Ghi chú không được để trống")
    private String note;
    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;
    @NotNull(message = "Id trạng thái không được để trống")
    private Long status;
    @NotNull(message = "Id nhóm không được để trống")
    private Long group;
}
