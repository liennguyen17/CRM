package com.example.democrm.controller;

import com.example.democrm.dto.StatisticsCustomerStatusDTO;
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
    ResponseEntity<?> getStatisticsCustomerStatus() {
        try {
            List<StatisticsCustomerStatusDTO> response = statisticsService.getCustomerStatusList();
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

}
