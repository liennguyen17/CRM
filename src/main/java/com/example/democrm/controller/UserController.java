package com.example.democrm.controller;

import com.example.democrm.constant.DateTimeConstant;
import com.example.democrm.constant.ErrorCodeDefs;
import com.example.democrm.dto.UserDTO;
import com.example.democrm.etity.User;
import com.example.democrm.request.user.CreateUserRequest;
import com.example.democrm.request.user.FilterUserRequest;
import com.example.democrm.request.user.UpdateUserRequest;
import com.example.democrm.response.BaseListItemResponse;
import com.example.democrm.response.BaseResponse;
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
    ResponseEntity<?> getAllUser() {
        try {
            List<UserDTO> response = userService.getAll();
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            UserDTO response = userService.getById(id);
            return buildItemResponse(response);
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @PostMapping("")
    ResponseEntity<?> creatUser(@Valid @RequestBody CreateUserRequest request) {
        try {
            UserDTO response = userService.createUser(request);
            return buildItemResponse(response);
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @PutMapping("/{id}")
    ResponseEntity<?> update(@Validated @RequestBody UpdateUserRequest request,
                             @PathVariable("id") Long id) throws ParseException {
        try {
            UserDTO response = userService.update(request, id);
            return buildItemResponse(response);
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            UserDTO response = userService.deleteById(id);
            return buildItemResponse(response);
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @DeleteMapping("/delete/all")
    ResponseEntity<?> deleteAllId(@RequestBody List<Long> ids) {
        List<UserDTO> response = userService.deleteAllId(ids);
        return buildListItemResponse(response, response.size());
    }

    @PostMapping("/filter")
    public ResponseEntity<?> filterUser(@Validated @RequestBody FilterUserRequest request) throws ParseException {
        Page<User> userPage = userService.filterUser(
                request,
                !Strings.isEmpty(request.getDateFrom()) ? MyUtils.convertDateFromString(request.getDateFrom(), DateTimeConstant.DATE_FORMAT) : null,
                !Strings.isEmpty(request.getDateTo()) ? MyUtils.convertDateFromString(request.getDateTo(), DateTimeConstant.DATE_FORMAT) : null
        );
        List<UserDTO> userDTOS = userPage.getContent().stream().map(
                user -> modelMapper.map(user, UserDTO.class)
        ).collect(Collectors.toList());
        return buildListItemResponse(userDTOS, userPage.getTotalElements());
    }

}
