package com.example.democrm.controller;

import com.example.democrm.constant.ErrorCodeDefs;
import com.example.democrm.dto.CustomerStatusDTO;
import com.example.democrm.etity.CustomerStatus;
import com.example.democrm.request.customerstatus.CreateCustomerStatusRequest;
import com.example.democrm.request.customerstatus.FilterCustomerStatusRequest;
import com.example.democrm.request.customerstatus.UpdateCustomerStatusRequest;
import com.example.democrm.response.BaseItemResponse;
import com.example.democrm.response.BaseListItemResponse;
import com.example.democrm.response.BaseResponse;
import com.example.democrm.service.CustomerStatusService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/status")
public class CustomerStatusController extends BaseController {
    private final CustomerStatusService customerStatusService;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public CustomerStatusController(CustomerStatusService customerStatusService) {
        this.customerStatusService = customerStatusService;
    }


    @GetMapping("/all")
    ResponseEntity<?> getAll() {
        try {
            List<CustomerStatusDTO> response = customerStatusService.getAll();
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }


    @GetMapping("/{id}")
    ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            CustomerStatusDTO response = customerStatusService.getById(id);
            return buildItemResponse(response);
        } catch (Exception ex) {
            return buildResponse();
        }
    }


    @PostMapping("")
    ResponseEntity<?> creat(@Valid @RequestBody CreateCustomerStatusRequest request) {
        try {
            CustomerStatusDTO response = customerStatusService.createCustomerStatus(request);
            return buildItemResponse(response);
        } catch (Exception ex) {
            return buildResponse();
        }
    }


    @PutMapping("/{id}")
    ResponseEntity<?> update(@Valid @RequestBody UpdateCustomerStatusRequest request,
                             @PathVariable("id") Long id) {
        try {
            CustomerStatusDTO response = customerStatusService.updateCustomerStatusById(request, id);
            return buildItemResponse(response);
        } catch (Exception ex) {
            return buildResponse();
        }
    }


    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            CustomerStatusDTO response = customerStatusService.deleteById(id);
            return buildItemResponse(response);
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @DeleteMapping("/deleteAll")
    ResponseEntity<?> deleteAll() {
        BaseResponse baseResponse = new BaseResponse();
        if (customerStatusService.deleteAll()) {
            baseResponse.setSuccess(true);
            return ResponseEntity.ok(baseResponse);
        } else {
            baseResponse.setSuccess(false);
            baseResponse.setFailed(ErrorCodeDefs.NOT_FOUND, ErrorCodeDefs.getErrMsg(ErrorCodeDefs.NOT_FOUND));
            return ResponseEntity.ok(baseResponse);
        }
    }

    @DeleteMapping("/delete/all")
    ResponseEntity<?> deleteAllId(@RequestBody List<Long> ids) {
        List<CustomerStatusDTO> response = customerStatusService.deleteAllId(ids);
        return buildListItemResponse(response, response.size());
    }

    @PostMapping("/list")
    public ResponseEntity<?> filterCustomerStatus(@Valid @RequestBody FilterCustomerStatusRequest request) {
        Page<CustomerStatus> page = customerStatusService.filterCustomerStatus(request);
        List<CustomerStatusDTO> response = page.getContent().stream().map(
                status -> modelMapper.map(status, CustomerStatusDTO.class)
        ).collect(Collectors.toList());
        return buildListItemResponse(response, page.getTotalElements());
    }

}
