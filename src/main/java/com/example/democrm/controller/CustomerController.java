package com.example.democrm.controller;

import com.example.democrm.constant.DateTimeConstant;
import com.example.democrm.dto.CustomersDTO;
import com.example.democrm.etity.Customers;
import com.example.democrm.request.customers.CreateCustomerRequest;
import com.example.democrm.request.customers.FilterCustomerRequest;
import com.example.democrm.request.customers.UpdateCustomerRequest;
import com.example.democrm.request.email.sendEmail;
import com.example.democrm.service.CustomersService;
import com.example.democrm.service.EmailService;
import com.example.democrm.utils.MyUtils;
import jakarta.mail.MessagingException;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customer")
public class CustomerController extends BaseController {
    private final CustomersService customersService;
    private final EmailService emailService;

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public CustomerController(CustomersService customersService, EmailService emailService) {
        this.customersService = customersService;
        this.emailService = emailService;
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
    ResponseEntity<?> getId(@PathVariable String id) {
        CustomersDTO response = customersService.getId(id);
        return buildItemResponse(response);
    }


    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('ADMIN','CREATE_CUSTOMER','STAFF')")
    ResponseEntity<?> create(@Valid @RequestBody CreateCustomerRequest request) {
        CustomersDTO response = customersService.createCustomers(request);
        return buildItemResponse(response);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','UPDATE_CUSTOMER','STAFF')")
    ResponseEntity<?> update(@Valid @RequestBody UpdateCustomerRequest request,
                             @PathVariable("id") Long id) {
        CustomersDTO response = customersService.updateCustomerById(request, id);
        return buildItemResponse(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','DELETE_CUSTOMER','STAFF')")
    ResponseEntity<?> deleteById(@PathVariable Long id) {
        CustomersDTO response = customersService.deleteById(id);
        return buildItemResponse(response);
    }


    @DeleteMapping("/delete/all")
    @PreAuthorize("hasAnyAuthority('ADMIN','DELETE_CUSTOMER','STAFF')")
    ResponseEntity<?> deleteAllId(@RequestBody List<Long> ids) {
        try {
            List<CustomersDTO> response = customersService.deleteList(ids);
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
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

    //khach hang bam trong email
    @GetMapping("/customer/{customerID}/status/{statusID}/update")
    public ResponseEntity<?> updateCustomerStatus(@PathVariable Long customerID, @PathVariable Long statusID){
        customersService.updateCustomerStatus(customerID, statusID);
        return ResponseEntity.ok("Trạng thái khách hàng đã được cập nhật!");
    }

//    @PostMapping("/send-request")
//    public ResponseEntity<?> sendRequest(@Valid @RequestBody sendEmail request) throws MessagingException {
//        emailService.sendEmailWithButtons(request);
////        forgotPasswordService.sendMessage(request);
//        String message = "Thành công";
//        return buildItemResponse(message);
//    }
}
