package com.example.democrm.controller;

import com.example.democrm.constant.DateTimeConstant;
import com.example.democrm.constant.ErrorCodeDefs;
import com.example.democrm.dto.CustomerGroupDTO;
import com.example.democrm.dto.CustomerStatusDTO;
import com.example.democrm.dto.CustomersDTO;
import com.example.democrm.etity.CustomerGroup;
import com.example.democrm.request.customergroup.CreateCustomerGroupRequest;
import com.example.democrm.request.customergroup.FilterCustomerGroupRequest;
import com.example.democrm.request.customergroup.UpdateCustomerGroupRequest;
import com.example.democrm.response.BaseItemResponse;
import com.example.democrm.response.BaseListItemResponse;
import com.example.democrm.response.BaseResponse;
import com.example.democrm.service.CustomerGroupService;
import com.example.democrm.utils.MyUtils;
import jakarta.validation.Valid;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/group")
public class CustomerGroupController extends BaseController {
    private final CustomerGroupService customerGroupService;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public CustomerGroupController(CustomerGroupService customerGroupService) {
        this.customerGroupService = customerGroupService;
    }


    @GetMapping("/all")
    ResponseEntity<?> getAll() {
        try {
            List<CustomerGroupDTO> response = customerGroupService.getAll();
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }


    @GetMapping("/{id}")
    ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            CustomerGroupDTO response = customerGroupService.getById(id);
            return buildItemResponse(response);
        } catch (Exception ex) {
            return buildResponse();
        }
    }


    @PostMapping("")
    ResponseEntity<?> create(@Valid @RequestBody CreateCustomerGroupRequest request) {
        try {
            CustomerGroupDTO response = customerGroupService.createGroup(request);
            return buildItemResponse(response);
        } catch (Exception ex) {
            return buildResponse();
        }
    }


    @PutMapping("/{id}")
    ResponseEntity<?> update(@Validated @RequestBody UpdateCustomerGroupRequest request,
                             @PathVariable("id") Long id) {
        try {
            CustomerGroupDTO response = customerGroupService.updateGroup(request, id);
            return buildItemResponse(response);
        } catch (Exception ex) {
            return buildResponse();
        }
    }


    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            CustomerGroupDTO response = customerGroupService.deleteById(id);
            return buildItemResponse(response);
        } catch (Exception ex) {
            return buildResponse();
        }
    }


    @DeleteMapping("/delete/all")
    ResponseEntity<?> deleteAllId(@RequestBody List<Long> ids) {
        try {
            List<CustomerGroupDTO> response = customerGroupService.deleteAllId(ids);
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @PostMapping("/filter")
    public ResponseEntity<?> filterCustomerGroup(@Validated @RequestBody FilterCustomerGroupRequest request) throws ParseException {
        Page<CustomerGroup> groupPage = customerGroupService.filterGroup(
                request,
                !Strings.isEmpty(request.getDateFrom()) ? MyUtils.convertDateFromString(request.getDateFrom(), DateTimeConstant.DATE_FORMAT) : null,
                !Strings.isEmpty(request.getDateTo()) ? MyUtils.convertDateFromString(request.getDateTo(), DateTimeConstant.DATE_FORMAT) : null
        );
        List<CustomerGroupDTO> customerGroupDTOS = groupPage.getContent().stream().map(
                customerGroup -> modelMapper.map(customerGroup, CustomerGroupDTO.class)
        ).collect(Collectors.toList());
        return buildListItemResponse(customerGroupDTOS, groupPage.getTotalElements());
    }


}
