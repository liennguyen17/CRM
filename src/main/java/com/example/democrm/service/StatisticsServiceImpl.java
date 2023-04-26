package com.example.democrm.service;

import com.example.democrm.dto.StatisticsCustomerStatusDTO;
import com.example.democrm.etity.CustomerStatus;
import com.example.democrm.etity.Customers;
import com.example.democrm.repository.CustomerStatusRepository;
import com.example.democrm.repository.CustomersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    private final CustomersRepository customersRepository;
    private final CustomerStatusRepository customerStatusRepository;

    @Autowired
    public StatisticsServiceImpl(CustomersRepository customersRepository, CustomerStatusRepository customerStatusRepository) {
        this.customersRepository = customersRepository;
        this.customerStatusRepository = customerStatusRepository;
    }

//    @Override
//    public List<StatisticsCustomerStatusDTO> statisticsCustomerStatus() {
//        List<Customers> customers = customersRepository.findAll();
//        List<CustomerStatus> statuses = customerStatusRepository.findAll();
//        Map<CustomerStatus, Integer> statusCountMap = new HashMap<>();
//
//        for (CustomerStatus status : statuses){
//            int count = 0;
//            for (Customers customer : customers){
//                if(customer.getCustomerStatus().equals(status)){
//                    count++;
//                }
//            }
//            statusCountMap.put(status, count);
//        }
//
//        int totalCustomers = customers.size();
//
//        List<StatisticsCustomerStatusDTO> customerStatusDTOS = new ArrayList<>();
//
//        for (CustomerStatus status : statuses){
//            int count = statusCountMap.get(status);
//            double percentage = ((double) count/totalCustomers) * 100;
//            StatisticsCustomerStatusDTO customerStatusDTO = new StatisticsCustomerStatusDTO();
//            customerStatusDTO.setType(status.getStatusName());
//            customerStatusDTO.setValue(percentage);
//            customerStatusDTOS.add(customerStatusDTO);
//        }
//        return customerStatusDTOS;
//    }

    @Override
    public List<StatisticsCustomerStatusDTO> getCustomerStatusList() {
        List<StatisticsCustomerStatusDTO> customerStatusDTOS = new ArrayList<>();
        List<CustomerStatus> statusList = customerStatusRepository.findAll();
        for (CustomerStatus status : statusList) {
            Long count = customersRepository.countByCustomerStatus(status);
            StatisticsCustomerStatusDTO dto = new StatisticsCustomerStatusDTO(status.getStatusName(), count.intValue());
            customerStatusDTOS.add(dto);
        }

        return customerStatusDTOS;
    }
}
