package com.example.democrm.controller;

import com.example.democrm.dto.StatisticsCustomerStatusDTO;
import com.example.democrm.dto.StatisticsStaffDTO;
import com.example.democrm.request.statistic.StatisticCustomerAndStaffRequest;
import com.example.democrm.service.StatisticsService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/statistics")
public class StatisticsController extends BaseController {
    private final StatisticsService statisticsService;
    private final ModelMapper modelMapper = new ModelMapper();

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    //thống kê tất cả các khách hàng theo trạng thái của tháng hiện tại
    @GetMapping("/customer/status")
    ResponseEntity<?> getCustomerStatusListPresentMonth() {
        try {
            List<StatisticsCustomerStatusDTO> response = statisticsService.getCustomerStatusListPresentMonth();
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    //thống kê tất cả khách hàng theo trạng thái của tháng trước(tính từ tháng hiện tại trở về trước)
    @GetMapping("/customer/status1")
    ResponseEntity<?> getCustomerStatusListPreviousMonth() {
        try {
            List<StatisticsCustomerStatusDTO> response = statisticsService.getCustomerStatusListPreviousMonth();
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    //thống kê nhân viên quản lý được bao nhiêu khách hàng thành công
    @GetMapping("/staff")
    ResponseEntity<?> getStatisticStaffByWeek() {
        try {
//            List<StatisticsStaffDTO> response = statisticsService.getStatisticStaffByWeek();
            List<StatisticsStaffDTO> response = statisticsService.getAllStatisticStaffByWeek();
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    //thống kê mỗi nhân viên có bao nhiêu khách hàng có trạng thái chưa xử lý
    @GetMapping("/staff1")
    ResponseEntity<?> getStatisticStaffByWeek1() {
        try {
//            List<StatisticsStaffDTO> response = statisticsService.getStatisticStaffByWeek1();
            List<StatisticsStaffDTO> response = statisticsService.getAllStatisticStaffByWeek1();
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    //thống kê tất cả các khách hàng theo trạng thái của tháng hiện tại ung voi nhan vien cu the
    @GetMapping("/customerOf/staff")
    ResponseEntity<?> getCustomerStatusListPresentMonthByStaff() {
        try {
            List<StatisticsCustomerStatusDTO> response = statisticsService.getCustomerStatusListPresentMonthByStaff();
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    //thống kê chọn thời gian tự động
    //trang admin
    //thống kê tất cả các khách hàng theo trạng thái
    @PostMapping("/customer/all-status")
    ResponseEntity<?> getCustomerStatusList(@RequestBody StatisticCustomerAndStaffRequest request) {
        try {
            List<StatisticsCustomerStatusDTO> response = statisticsService.getCustomerStatusList(request);
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    //thống kê mỗi nhân viên quản lý được bao nhiêu khách hàng có trạng thái thành công theo khoảng thời gian tự truyền
    @PostMapping("/staff/status-success")
    ResponseEntity<?> getAllStatusSuccessCustomer_Staff(@RequestBody StatisticCustomerAndStaffRequest request) {
        try {
//            List<StatisticsStaffDTO> response = statisticsService.getStatisticStaffByWeek();
            List<StatisticsStaffDTO> response = statisticsService.getAllStatusSuccessCustomer_Staff(request);
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    //thống kê mỗi nhân viên quản lý được bao nhiêu khách hàng có trạng thái thành công theo khoảng thời gian tự truyền
    @PostMapping("/staff/status-4")
    ResponseEntity<?> getAllStatus4Customer_Staff(@RequestBody StatisticCustomerAndStaffRequest request) {
        try {
//            List<StatisticsStaffDTO> response = statisticsService.getStatisticStaffByWeek();
            List<StatisticsStaffDTO> response = statisticsService.getAllStatus4Customer_Staff(request);
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    //trang staff
    //thống kê tất cả các khách hàng theo trạng thái của tháng hiện tại ung voi nhan vien cu the
    @PostMapping("/customerOf/staff1")
    ResponseEntity<?> getCustomerStatusListPageStaff(@RequestBody StatisticCustomerAndStaffRequest request) {
        try {
            List<StatisticsCustomerStatusDTO> response = statisticsService.getCustomerStatusListPageStaff(request);
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

}
