package com.example.democrm.service;

import com.example.democrm.dto.StatisticsCustomerStatusDTO;
import com.example.democrm.dto.StatisticsStaffDTO;

import java.util.List;

public interface StatisticsService {
    //thống kê tất cả các khách hàng theo trạng thái của tháng hiện tại
    List<StatisticsCustomerStatusDTO> getCustomerStatusListPresentMonth();

    //thống kê tất cả khách hàng theo trạng thái của tháng trước(tính từ tháng hiện tại trở về trước)
    List<StatisticsCustomerStatusDTO> getCustomerStatusListPreviousMonth();

    //thống kê nhân viên quản ý được bao nhiêu khách hàng tiềm năng theo tuần
    List<StatisticsStaffDTO> getStatisticStaffByWeek();

    //thống kê nhân viên quản ý được bao nhiêu khách hàng chưa xử lý theo tuần
    List<StatisticsStaffDTO> getStatisticStaffByWeek1();

    //thống kê mỗi nhân viên quản lý được bao nhiêu khách hàng có trạng thái thành công theo tuần
    List<StatisticsStaffDTO> getAllStatisticStaffByWeek();

    //thống kê mỗi nhân viên quản lý được bao nhiêu khách hàng có trạng thái chưa xử lý theo tuần
    List<StatisticsStaffDTO> getAllStatisticStaffByWeek1();
}
