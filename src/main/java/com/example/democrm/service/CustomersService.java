package com.example.democrm.service;

import com.example.democrm.dto.CustomersDTO;
import com.example.democrm.etity.Customers;
import com.example.democrm.request.customers.CreateCustomerRequest;
import com.example.democrm.request.customers.FilterCustomerRequest;
import com.example.democrm.request.customers.UpdateCustomerRequest;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface CustomersService {
    List<CustomersDTO> getAll();

    CustomersDTO getById(Long id);

    CustomersDTO createCustomers(CreateCustomerRequest request);

    CustomersDTO updateCustomerById(UpdateCustomerRequest request, Long id);

    CustomersDTO deleteById(Long id);

//    boolean deleteAll();

    List<CustomersDTO> deleteList(List<Long> ids);

    Page<Customers> filterCustomer(FilterCustomerRequest request, Date dateFrom, Date dateTo);
}
