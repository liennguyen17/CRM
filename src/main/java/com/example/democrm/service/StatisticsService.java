package com.example.democrm.service;

import com.example.democrm.dto.StatisticsCustomerStatusDTO;
import com.example.democrm.dto.StatisticsStaffDTO;
import com.example.democrm.request.statistic.StatisticCustomerAndStaffRequest;

import java.text.ParseException;
import java.util.List;

public interface StatisticsService {
    //thong ke trang admin
    //thống kê tất cả các khách hàng theo trạng thái của tháng hiện tại
    List<StatisticsCustomerStatusDTO> getCustomerStatusListPresentMonth();

    //thống kê tất cả khách hàng theo trạng thái của tháng trước(tính từ tháng hiện tại trở về trước)
    List<StatisticsCustomerStatusDTO> getCustomerStatusListPreviousMonth();

    //thống kê mỗi nhân viên quản lý được bao nhiêu khách hàng có trạng thái thành công theo tuần
    List<StatisticsStaffDTO> getAllStatisticStaffByWeek();

    //thống kê mỗi nhân viên quản lý được bao nhiêu khách hàng có trạng thái chưa xử lý theo tuần
    List<StatisticsStaffDTO> getAllStatisticStaffByWeek1();

    //thong ke trang nhan vien
    //thống kê tất cả các khách hàng theo trạng thái của tháng hiện tại ung voi nhan vien cu the
    List<StatisticsCustomerStatusDTO> getCustomerStatusListPresentMonthByStaff();

    //thống kê truyền tháng tự động
    //trang admin
    //thống kê tất cả các khách hàng theo trạng thái
    List<StatisticsCustomerStatusDTO> getCustomerStatusList(StatisticCustomerAndStaffRequest request) throws ParseException;

    //thống kê mỗi nhân viên quản lý được bao nhiêu khách hàng có trạng thái thành công
    List<StatisticsStaffDTO> getAllStatusSuccessCustomer_Staff(StatisticCustomerAndStaffRequest request) throws ParseException;

    //thống kê mỗi nhân viên quản lý được bao nhiêu khách hàng có trạng thái chưa xử lý theo tuần
    List<StatisticsStaffDTO> getAllStatus4Customer_Staff(StatisticCustomerAndStaffRequest request) throws ParseException;

    //trang staff

    //thống kê tất cả các khách hàng theo trạng thái của tháng hiện tại ung voi nhan vien cu the
    List<StatisticsCustomerStatusDTO> getCustomerStatusListPageStaff(StatisticCustomerAndStaffRequest request) throws ParseException;



}
