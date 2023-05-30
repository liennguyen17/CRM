package com.example.democrm.service;

import com.example.democrm.dto.CustomerGroupDTO;
import com.example.democrm.etity.CustomerGroup;
import com.example.democrm.etity.User;
import com.example.democrm.repository.CustomCustomerGroupRepository;
import com.example.democrm.repository.CustomerGroupRepository;
import com.example.democrm.repository.UserRepository;
import com.example.democrm.request.customergroup.CreateCustomerGroupRequest;
import com.example.democrm.request.customergroup.FilterCustomerGroupRequest;
import com.example.democrm.request.customergroup.UpdateCustomerGroupRequest;
import jakarta.persistence.EntityNotFoundException;
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
public class CustomerGroupServiceImpl implements CustomerGroupService {
    private final CustomerGroupRepository customerGroupRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public CustomerGroupServiceImpl(CustomerGroupRepository customerGroupRepository, UserRepository userRepository) {
        this.customerGroupRepository = customerGroupRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<CustomerGroupDTO> getAll() {
        return customerGroupRepository.findAll().stream().map(
                customerGroup -> modelMapper.map(customerGroup, CustomerGroupDTO.class)
        ).collect(Collectors.toList());
    }

    @Override
    public CustomerGroupDTO getById(String idStr) {
        Long id;
        try {
            id = Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Tham số truyền vào không hợp lệ");
        }
        Optional<CustomerGroup> customerGroup = customerGroupRepository.findById(id);
        if (customerGroup.isPresent()) {
            return modelMapper.map(customerGroupRepository.findById(id), CustomerGroupDTO.class);
        } else {
            throw new RuntimeException("Id nhóm khách hàng không tồn tại trong hệ thống!");
        }
    }

    @Override
    @Transactional
    public CustomerGroupDTO createGroup(CreateCustomerGroupRequest request) {
        try {
            Optional<User> userOptional = userRepository.findById(request.getUserId());
            CustomerGroup customerGroup = CustomerGroup.builder()
                    .groupName(request.getGroupName())
                    .createdDate(new Timestamp(System.currentTimeMillis()))
                    .updateDate(new Timestamp(System.currentTimeMillis()))
                    .build();
            customerGroup.setUser(userOptional.get());
            customerGroup = customerGroupRepository.saveAndFlush(customerGroup);
            return modelMapper.map(customerGroup, CustomerGroupDTO.class);
        } catch (Exception ex) {
            throw new RuntimeException("Có lỗi xảy ra trong quá trình tạo nhóm khách hàng mới!");
        }
    }

    @Override
    @Transactional
    public CustomerGroupDTO updateGroup(UpdateCustomerGroupRequest request, Long id) {
        if (!customerGroupRepository.existsById(id)) {
            throw new EntityNotFoundException("Nhóm khách hàng có id:" + id + " cần cập nhật không tồn tại trong hệ thống!");
        }
        Optional<CustomerGroup> customerGroupOptional = customerGroupRepository.findById(id);
        Optional<User> userOptional = userRepository.findById(request.getUserId());
        if (customerGroupOptional.isPresent()) {
            customerGroupRepository.findById(id).stream().map(
                    customerGroup -> {
                        customerGroup.setGroupName(request.getGroupName());
                        customerGroup.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                        customerGroup.setUser(userOptional.get());
                        return customerGroupRepository.save(customerGroup);
                    }
            ).collect(Collectors.toList());
            return modelMapper.map(customerGroupOptional, CustomerGroupDTO.class);
        }
        throw new RuntimeException("Có lỗi xảy ra trong quá trình cập nhật nhóm khách hàng");
    }

    @Override
    @Transactional
    public CustomerGroupDTO deleteById(Long id) {
        if (!customerGroupRepository.existsById(id)) {
            throw new EntityNotFoundException("Nhóm khách hàng có id:" + id + " cần xóa không tồn tại trong hệ thống!");
        }
        Optional<CustomerGroup> customerGroup = customerGroupRepository.findById(id);
        if (customerGroup.isPresent()) {
            customerGroupRepository.deleteById(id);
            return modelMapper.map(customerGroup, CustomerGroupDTO.class);
        }
        throw new RuntimeException("Có lỗi xảy ra trong quá trình xóa nhóm khách hàng");
    }

    @Override
    @Transactional
    public List<CustomerGroupDTO> deleteAllId(List<Long> ids) {
        List<CustomerGroupDTO> customerGroupDTOS = new ArrayList<>();
        for (CustomerGroup group : customerGroupRepository.findAllById(ids)) {
            customerGroupDTOS.add(modelMapper.map(group, CustomerGroupDTO.class));
        }
        customerGroupRepository.deleteAllByIdInBatch(ids);
        return customerGroupDTOS;
    }

    @Override
    public Page<CustomerGroup> filterGroup(FilterCustomerGroupRequest request, Date dateFrom, Date dateTo) {
        Specification<CustomerGroup> specification = CustomCustomerGroupRepository.filterSpecification(dateFrom, dateTo, request);
        Page<CustomerGroup> customerGroupPage = customerGroupRepository.findAll(specification, PageRequest.of(request.getStart(), request.getLimit()));
        return customerGroupPage;
    }
}
