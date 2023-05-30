package com.example.democrm.controller;

import com.example.democrm.dto.StatisticsCustomerStatusDTO;
import com.example.democrm.dto.StatisticsStaffDTO;
import com.example.democrm.service.StatisticsService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistics")
public class StatisticsController extends BaseController {
    private final StatisticsService statisticsService;
    private final ModelMapper modelMapper = new ModelMapper();

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/customer/status")
    ResponseEntity<?> getCustomerStatusListPresentMonth() {
        try {
            List<StatisticsCustomerStatusDTO> response = statisticsService.getCustomerStatusListPresentMonth();
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @GetMapping("/customer/status1")
    ResponseEntity<?> getCustomerStatusListPreviousMonth() {
        try {
            List<StatisticsCustomerStatusDTO> response = statisticsService.getCustomerStatusListPreviousMonth();
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    //thống kê nhân viên quản lý được bao nhiêu khách hàng tiềm năng
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

}
