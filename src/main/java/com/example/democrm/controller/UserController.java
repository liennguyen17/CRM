package com.example.democrm.controller;

import com.example.democrm.constant.DateTimeConstant;
import com.example.democrm.dto.CustomerGroupDTO;
import com.example.democrm.dto.CustomersDTO;
import com.example.democrm.dto.UserDTO;
import com.example.democrm.etity.User;
import com.example.democrm.request.user.CreateUserRequest;
import com.example.democrm.request.user.FilterUserRequest;
import com.example.democrm.request.user.ListUserManagerRequest;
import com.example.democrm.request.user.UpdateUserRequest;
import com.example.democrm.service.UserService;
import com.example.democrm.utils.MyUtils;
import jakarta.validation.Valid;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
    private final UserService userService;
    private final ModelMapper modelMapper = new ModelMapper();

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    ResponseEntity<?> getAllUser() {
        try {
            List<UserDTO> response = userService.getAll();
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    ResponseEntity<?> getById(@PathVariable String id) {
        UserDTO response = userService.getById(id);
        return buildItemResponse(response);
    }

    @PostMapping("")
//    @PreAuthorize("hasAnyAuthority('CREATE_USER','ADMIN')")
    ResponseEntity<?> creatUser(@Valid @RequestBody CreateUserRequest request) throws ParseException {
        UserDTO response = userService.createUser(request);
        return buildItemResponse(response);
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyAuthority('UPDATE_USER', 'ADMIN')")
    ResponseEntity<?> update(@Validated @RequestBody UpdateUserRequest request,
                             @PathVariable("id") Long id) throws ParseException {
        UserDTO response = userService.update(request, id);
        return buildItemResponse(response);
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyAuthority('ADMIN','DELETE_USER')")
    ResponseEntity<?> deleteById(@PathVariable Long id) {
        UserDTO response = userService.deleteById(id);
        return buildItemResponse(response);
    }

    @DeleteMapping("/delete/all")
//    @PreAuthorize("hasAnyAuthority('ADMIN','DELETE_USER')")
    ResponseEntity<?> deleteAllId(@RequestBody List<Long> ids) {
        try {
            List<UserDTO> response = userService.deleteAllId(ids);
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @PostMapping("/filter")
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> filterUser(@Validated @RequestBody FilterUserRequest request) throws ParseException {
        Page<User> userPage = userService.filterUser(
                request,
                !Strings.isEmpty(request.getDateFrom()) ? MyUtils.convertDateFromString(request.getDateFrom(), DateTimeConstant.DATE_FORMAT) : null,
                !Strings.isEmpty(request.getDateTo()) ? MyUtils.convertDateFromString(request.getDateTo(), DateTimeConstant.DATE_FORMAT) : null,
                !Strings.isEmpty(request.getDateOfBirthFrom()) ? MyUtils.convertDateFromString(request.getDateOfBirthFrom(), DateTimeConstant.DATE_FORMAT) : null,
                !Strings.isEmpty(request.getDateOfBirthTo()) ? MyUtils.convertDateFromString(request.getDateOfBirthTo(), DateTimeConstant.DATE_FORMAT) : null
        );
        List<UserDTO> userDTOS = userPage.getContent().stream().map(
                user -> modelMapper.map(user, UserDTO.class)
        ).collect(Collectors.toList());
        return buildListItemResponse(userDTOS, userPage.getTotalElements());
    }

    //trang admin
    //Lấy danh sách khách hàng ứng với nhân viên trong trang admin
    @GetMapping("/manager/customers")
    ResponseEntity<?> getUserManagedCustomersPageAdmin(@RequestBody ListUserManagerRequest request){
        List<CustomersDTO> customersDTOList = userService.getUserManagedCustomersPageAdmin(request.getUserId());
        return buildListItemResponse(customersDTOList, customersDTOList.size());
    }

    @GetMapping("/manager/group")
    ResponseEntity<?> getUserManagedCustomerGroupsPageAdmin(@RequestBody ListUserManagerRequest request) {
        List<CustomerGroupDTO> customerGroupDTOs = userService.getUserManagedCustomerGroupsPageAdmin(request.getUserId());
        return buildListItemResponse(customerGroupDTOs, customerGroupDTOs.size());
    }

    //Lấy danh sách khách hàng ứng với nhân viên trong trang admin
    @GetMapping("/manager/customers/{id}")
    ResponseEntity<?> getUserManagedCustomersPageAdmin(@PathVariable Long id){
        List<CustomersDTO> customersDTOList = userService.getUserManagedCustomersPageAdmin(id);
        return buildListItemResponse(customersDTOList, customersDTOList.size());
    }

    @GetMapping("/manager/group/{id}")
    ResponseEntity<?> getUserManagedCustomerGroupsPageAdmin(@PathVariable Long id) {
        List<CustomerGroupDTO> customerGroupDTOs = userService.getUserManagedCustomerGroupsPageAdmin(id);
        return buildListItemResponse(customerGroupDTOs, customerGroupDTOs.size());
    }



    //trang Nhân viên
    //thống kê user quản lý những nhóm nào
    //@PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/manager-group")
    ResponseEntity<?> getUserManagedCustomerGroups() {
        List<CustomerGroupDTO> customerGroupDTOs = userService.getUserManagedCustomerGroups();
        return buildListItemResponse(customerGroupDTOs, customerGroupDTOs.size());
    }

    //thống kê user quản lý số khách hàng thuộc nhóm đang quản lý
    @GetMapping("/manager-customer")
    ResponseEntity<?> getUserManagedCustomer(){
        List<CustomersDTO> customersDTOList = userService.getUserManagedCustomer();
        return buildListItemResponse(customersDTOList, customersDTOList.size());
    }

    //thống kê nhân viên(user) quản lý khách hàng của nhân viên đó
    @GetMapping("/manager-customers")
    ResponseEntity<?> getUserManagedCustomers(){
        List<CustomersDTO> customersDTOList = userService.getUserManagedCustomers();
        return buildListItemResponse(customersDTOList, customersDTOList.size());
    }



}
