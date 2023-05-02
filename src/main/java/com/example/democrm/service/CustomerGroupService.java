package com.example.democrm.service;

import com.example.democrm.dto.CustomerGroupDTO;
import com.example.democrm.dto.CustomersDTO;
import com.example.democrm.etity.CustomerGroup;
import com.example.democrm.request.customergroup.CreateCustomerGroupRequest;
import com.example.democrm.request.customergroup.FilterCustomerGroupRequest;
import com.example.democrm.request.customergroup.UpdateCustomerGroupRequest;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface CustomerGroupService {
    List<CustomerGroupDTO> getAll();

    CustomerGroupDTO getById(String idStr);

    CustomerGroupDTO createGroup(CreateCustomerGroupRequest request);

    CustomerGroupDTO updateGroup(UpdateCustomerGroupRequest request, Long id);

    CustomerGroupDTO deleteById(Long id);

    List<CustomerGroupDTO> deleteAllId(List<Long> ids);

//    boolean deleteAll();

    Page<CustomerGroup> filterGroup(FilterCustomerGroupRequest request, Date dateFrom, Date dateTo);
}
