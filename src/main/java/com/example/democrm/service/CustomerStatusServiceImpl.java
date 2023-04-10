package com.example.democrm.service;

import com.example.democrm.dto.CustomerStatusDTO;
import com.example.democrm.etity.CustomerStatus;
import com.example.democrm.repository.CustomCustomerStatusRepository;
import com.example.democrm.repository.CustomerStatusRepository;
import com.example.democrm.request.customerstatus.CreateCustomerStatusRequest;
import com.example.democrm.request.customerstatus.FilterCustomerStatusRequest;
import com.example.democrm.request.customerstatus.UpdateCustomerStatusRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerStatusServiceImpl implements CustomerStatusService {
    private final CustomerStatusRepository customerStatusRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public CustomerStatusServiceImpl(CustomerStatusRepository customerStatusRepository) {
        this.customerStatusRepository = customerStatusRepository;
    }


    @Override
    public List<CustomerStatusDTO> getAll() {
        return customerStatusRepository.findAll().stream().map(
                status -> modelMapper.map(status, CustomerStatusDTO.class)
        ).collect(Collectors.toList());
    }

    @Override
    public CustomerStatusDTO getById(Long id) {
        return modelMapper.map(customerStatusRepository.findById(id), CustomerStatusDTO.class);
    }

    @Override
    @Transactional
    public CustomerStatusDTO createCustomerStatus(CreateCustomerStatusRequest request) {
        CustomerStatus customerStatus = CustomerStatus.builder()
                .statusName(request.getStatusName())
                .build();
        customerStatus = customerStatusRepository.saveAndFlush(customerStatus);
        return modelMapper.map(customerStatus, CustomerStatusDTO.class);
    }

    @Override
    @Transactional
    public CustomerStatusDTO updateCustomerStatusById(UpdateCustomerStatusRequest request, Long id) {
        Optional<CustomerStatus> customerStatus = customerStatusRepository.findById(id);
        if (customerStatus.isPresent()) {
            customerStatusRepository.findById(id).stream().map(
                    status -> {
                        status.setStatusName(request.getStatusName());
                        return customerStatusRepository.save(status);
                    }
            ).collect(Collectors.toList());
            return modelMapper.map(customerStatus, CustomerStatusDTO.class);
        }
        throw new RuntimeException("Có lỗi xảy ra trong quá trình cập nhật trạng thái");
    }

    @Override
    @Transactional
    public CustomerStatusDTO deleteById(Long id) {
        Optional<CustomerStatus> customerStatus = customerStatusRepository.findById(id);
        if (customerStatus.isPresent()) {
            customerStatusRepository.deleteById(id);
            return modelMapper.map(customerStatus, CustomerStatusDTO.class);
        }
        throw new RuntimeException("Có lỗi xảy ra trong quá trình xóa trạng thái");
//        return null;
    }

    @Override
    public List<CustomerStatusDTO> deleteAllId(List<Long> ids) {
        List<CustomerStatusDTO> customerStatusDTOS = new ArrayList<>();
        for (CustomerStatus customerStatus : customerStatusRepository.findAllById(ids)) {
            customerStatusDTOS.add(modelMapper.map(customerStatus, CustomerStatusDTO.class));
        }
        customerStatusRepository.deleteAllByIdInBatch(ids);
        return customerStatusDTOS;
    }

    @Override
    @Transactional
    public boolean deleteAll() {
        Optional<List<CustomerStatus>> customerStatuses = Optional.of(customerStatusRepository.findAll());
        if (customerStatuses.isPresent()) {
            customerStatusRepository.deleteAll();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Page<CustomerStatus> filterCustomerStatus(FilterCustomerStatusRequest request) {
        Specification<CustomerStatus> specification = CustomCustomerStatusRepository.buildFilterSpecification(request.getStatusName());
        return customerStatusRepository.findAll(specification, PageRequest.of(request.getStart(), request.getLimit()));
    }


}
