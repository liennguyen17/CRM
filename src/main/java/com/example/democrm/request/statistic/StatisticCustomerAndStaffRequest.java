package com.example.democrm.request.statistic;

import com.example.democrm.validator.DateValidateAnnotation;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StatisticCustomerAndStaffRequest {
    @NotBlank(message = "Ngày bắt đầu không được để trống!")
    @DateValidateAnnotation(message = "Định dạng ngày tháng phải là dd/MM/YYYY")
    private String dateFrom;

    @NotBlank(message = "Ngày kết thúc không được để trống!")
    @DateValidateAnnotation(message = "Định dạng ngày tháng phải là dd/MM/YYYY")
    private String dateTo;
}
