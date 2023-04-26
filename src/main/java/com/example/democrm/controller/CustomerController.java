package com.example.democrm.controller;

import com.example.democrm.constant.DateTimeConstant;
import com.example.democrm.constant.ErrorCodeDefs;
import com.example.democrm.dto.CustomerStatusDTO;
import com.example.democrm.dto.CustomersDTO;
import com.example.democrm.etity.Customers;
import com.example.democrm.request.customers.CreateCustomerRequest;
import com.example.democrm.request.customers.FilterCustomerRequest;
import com.example.democrm.request.customers.UpdateCustomerRequest;
import com.example.democrm.response.BaseItemResponse;
import com.example.democrm.response.BaseListItemResponse;
import com.example.democrm.response.BaseResponse;
import com.example.democrm.service.CustomersService;
import com.example.democrm.utils.MyUtils;
import jakarta.validation.Valid;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customer")
public class CustomerController extends BaseController {
    private final CustomersService customersService;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public CustomerController(CustomersService customersService) {
        this.customersService = customersService;
    }


    @GetMapping("/all")
    ResponseEntity<?> getAll() {
        try {
            List<CustomersDTO> response = customersService.getAll();
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }


    @GetMapping("/{id}")
    ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            CustomersDTO response = customersService.getById(id);
            return buildItemResponse(response);
        } catch (Exception ex) {
            return buildResponse();
        }
    }


    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('ADMIN','CREATE_CUSTOMER','STAFF')")
    ResponseEntity<?> create(@Valid @RequestBody CreateCustomerRequest request) {
        try {
            CustomersDTO response = customersService.createCustomers(request);
            return buildItemResponse(response);
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','UPDATE_CUSTOMER','STAFF')")
    ResponseEntity<?> update(@Valid @RequestBody UpdateCustomerRequest request,
                             @PathVariable("id") Long id) {
        try {
            CustomersDTO response = customersService.updateCustomerById(request, id);
            return buildItemResponse(response);
        } catch (Exception ex) {
            return buildResponse();
        }
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','DELETE_CUSTOMER')")
    ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            CustomersDTO response = customersService.deleteById(id);
            return buildItemResponse(response);
        } catch (Exception ex) {
            return buildResponse();
        }
    }


    @DeleteMapping("/delete/all")
    @PreAuthorize("hasAnyAuthority('ADMIN','DELETE_CUSTOMER')")
    ResponseEntity<?> deleteAllId(@RequestBody List<Long> ids) {
        List<CustomersDTO> response = customersService.deleteList(ids);
        return buildListItemResponse(response, response.size());
    }


    @PostMapping("/filter")
    public ResponseEntity<?> filterCustomer(@Validated @RequestBody FilterCustomerRequest request) throws ParseException {
        Page<Customers> customersPage = customersService.filterCustomer(
                request,
                !Strings.isEmpty(request.getDateFrom()) ? MyUtils.convertDateFromString(request.getDateFrom(), DateTimeConstant.DATE_FORMAT) : null,
                !Strings.isEmpty(request.getDateTo()) ? MyUtils.convertDateFromString(request.getDateTo(), DateTimeConstant.DATE_FORMAT) : null
        );

        List<CustomersDTO> customersDTOS = customersPage.getContent().stream().map(
                customers -> modelMapper.map(customers, CustomersDTO.class)
        ).collect(Collectors.toList());
        return buildListItemResponse(customersDTOS, customersPage.getTotalElements());
    }
}
