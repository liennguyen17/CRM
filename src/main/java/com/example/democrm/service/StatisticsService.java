package com.example.democrm.service;

import com.example.democrm.dto.StatisticsCustomerStatusDTO;

import java.util.List;

public interface StatisticsService {
    List<StatisticsCustomerStatusDTO> getCustomerStatusList();
}
