package com.example.democrm.service;

import com.example.democrm.dto.CustomerGroupDTO;
import com.example.democrm.dto.CustomersDTO;
import com.example.democrm.dto.UserDTO;
import com.example.democrm.etity.CustomerGroup;
import com.example.democrm.etity.User;
import com.example.democrm.request.user.CreateUserRequest;
import com.example.democrm.request.user.FilterUserRequest;
import com.example.democrm.request.user.UpdateUserRequest;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface UserService {
    List<UserDTO> getAll();

    UserDTO getById(String idStr);

    UserDTO createUser(CreateUserRequest request) throws ParseException;

    UserDTO update(UpdateUserRequest request, Long id) throws ParseException;

    UserDTO deleteById(Long id);

    List<UserDTO> deleteAllId(List<Long> ids);

    Page<User> filterUser(FilterUserRequest request, Date dateFrom, Date dateTo, Date dateOfBirthFrom, Date dateOfBirthTo);

    List<CustomerGroupDTO> getUserManagedCustomerGroups();

    List<CustomersDTO> getUserManagedCustomer();

    List<CustomersDTO> getUserManagedCustomers();
}
