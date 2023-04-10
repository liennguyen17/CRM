package com.example.democrm.service;

import com.example.democrm.dto.CustomersDTO;
import com.example.democrm.etity.CustomerGroup;
import com.example.democrm.etity.CustomerStatus;
import com.example.democrm.etity.Customers;
import com.example.democrm.repository.CustomCustomerRepository;
import com.example.democrm.repository.CustomerGroupRepository;
import com.example.democrm.repository.CustomerStatusRepository;
import com.example.democrm.repository.CustomersRepository;
import com.example.democrm.request.customers.CreateCustomerRequest;
import com.example.democrm.request.customers.FilterCustomerRequest;
import com.example.democrm.request.customers.UpdateCustomerRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomersServiceImpl implements CustomersService {
    private final CustomersRepository customersRepository;

    private final CustomerStatusRepository customerStatusRepository;
    private final CustomerGroupRepository customerGroupRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public CustomersServiceImpl(CustomersRepository customersRepository, CustomerStatusRepository customerStatusRepository, CustomerGroupRepository customerGroupRepository) {
        this.customersRepository = customersRepository;
        this.customerStatusRepository = customerStatusRepository;
        this.customerGroupRepository = customerGroupRepository;
    }


    @Override
    public List<CustomersDTO> getAll() {
        return customersRepository.findAll().stream().map(
                customers -> modelMapper.map(customers, CustomersDTO.class)
        ).collect(Collectors.toList());
    }

    @Override
    public CustomersDTO getById(Long id) {
        return modelMapper.map(customersRepository.findById(id), CustomersDTO.class);
    }

    @Override
    @Transactional
    public CustomersDTO createCustomers(CreateCustomerRequest request) {
        Optional<CustomerStatus> customerStatusOptional = customerStatusRepository.findById(request.getStatus());
        Optional<CustomerGroup> customerGroupOptional = customerGroupRepository.findById(request.getGroup());
        Customers customers = Customers.builder()
                .customerName(request.getCustomerName())
                .createdDate(new Timestamp(System.currentTimeMillis()))
                .phone(request.getPhone())
                .email(request.getEmail())
                .note(request.getNote())
                .address(request.getAddress())
                .build();
        customers.setCustomerStatus(customerStatusOptional.get());
        customers.setCustomerGroup(customerGroupOptional.get());
        customers = customersRepository.saveAndFlush(customers);
        return modelMapper.map(customers, CustomersDTO.class);
    }

//    @Override
//    @Transactional
//    public CustomersDTO updateCustomerById(UpdateCustomerRequest request, Long id) {
//        Optional<Customers> customersOptional = customersRepository.findById(id);
//        Optional<CustomerStatus> customerStatusOptional = customerStatusRepository.findById(request.getStatus());
//        if (customersOptional.isPresent()) {
//            customersRepository.findById(id).stream().map(
//                    customers -> {
//                        customers.setCustomerName(request.getCustomerName());
//                        customers.setPhone(request.getPhone());
//                        customers.setEmail(request.getEmail());
//                        customers.setNote(request.getNote());
//                        customers.setAddress(request.getAddress());
//
//                        return customersRepository.save(customers);
//                    }
//            ).collect(Collectors.toList());
//            return modelMapper.map(customersOptional, CustomersDTO.class);
//        }
//        throw new RuntimeException("Có lỗi xảy ra trong quá trình cập nhật khách hàng");
////        return null;
//    }

    @Override
    @Transactional
    public CustomersDTO updateCustomerById(UpdateCustomerRequest request, Long id) {
        Optional<Customers> customersOptional = customersRepository.findById(id);
        Optional<CustomerStatus> customerStatusOptional = customerStatusRepository.findById(request.getStatus());
        Optional<CustomerGroup> customerGroupOptional = customerGroupRepository.findById(request.getGroup());
        if (customersOptional.isPresent()) {
            Customers customers = customersOptional.get();
            customers.setCustomerName(request.getCustomerName());
            customers.setPhone(request.getPhone());
            customers.setEmail(request.getEmail());
            customers.setNote(request.getNote());
            customers.setAddress(request.getAddress());
            customers.setCustomerStatus(customerStatusOptional.get());
            customers.setCustomerGroup(customerGroupOptional.get());
            return modelMapper.map(customersRepository.save(customers), CustomersDTO.class);
        }
        throw new RuntimeException("Có lỗi xảy ra trong quá trình cập nhật khách hàng");
//        return null;
    }

    @Override
    @Transactional
    public CustomersDTO deleteById(Long id) {
        Optional<Customers> customersOptional = customersRepository.findById(id);
        if (customersOptional.isPresent()) {
            customersRepository.deleteById(id);
            return modelMapper.map(customersOptional, CustomersDTO.class);
        }
        throw new RuntimeException("Có lỗi xảy ra trong quá trình xóa khách hàng");
//        return null;
    }

//    @Override
//    @Transactional
//    public boolean deleteAll() {
//        Optional<List<Customers>> optionalCustomers = Optional.of(customersRepository.findAll());
//        if (optionalCustomers.isPresent()) {
//            customersRepository.deleteAll();
//            return true;
//        } else {
//            return false;
//        }
//    }

    @Override
    @Transactional
    public List<CustomersDTO> deleteList(List<Long> ids) {
        List<CustomersDTO> customersDTOS = new ArrayList<>();
        for (Customers customers : customersRepository.findAllById(ids)) {
            customersDTOS.add(modelMapper.map(customers, CustomersDTO.class));
        }
        customersRepository.deleteAllByIdInBatch(ids);
        return customersDTOS;
    }

    @Override
    public Page<Customers> filterCustomer(FilterCustomerRequest request, Date dateFrom, Date dateTo) {
        Specification<Customers> specification = CustomCustomerRepository.filterSpecification(dateFrom, dateTo, request);
        Page<Customers> customersPage = customersRepository.findAll(specification, PageRequest.of(request.getStart(), request.getLimit()));
        return customersPage;
    }

}