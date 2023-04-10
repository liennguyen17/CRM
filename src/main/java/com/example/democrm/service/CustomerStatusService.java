package com.example.democrm.service;

import com.example.democrm.dto.CustomerStatusDTO;
import com.example.democrm.etity.CustomerStatus;
import com.example.democrm.request.customerstatus.CreateCustomerStatusRequest;
import com.example.democrm.request.customerstatus.FilterCustomerStatusRequest;
import com.example.democrm.request.customerstatus.UpdateCustomerStatusRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerStatusService {
    List<CustomerStatusDTO> getAll();

    CustomerStatusDTO getById(Long id);

    CustomerStatusDTO createCustomerStatus(CreateCustomerStatusRequest request);

    CustomerStatusDTO updateCustomerStatusById(UpdateCustomerStatusRequest request, Long id);

    CustomerStatusDTO deleteById(Long id);

    List<CustomerStatusDTO> deleteAllId(List<Long> ids);

    boolean deleteAll();

    Page<CustomerStatus> filterCustomerStatus(FilterCustomerStatusRequest request);

}
