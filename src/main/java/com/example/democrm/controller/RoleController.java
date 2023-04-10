package com.example.democrm.controller;

import com.example.democrm.constant.DateTimeConstant;
import com.example.democrm.dto.RoleDTO;
import com.example.democrm.etity.Role;
import com.example.democrm.request.role.CreateRoleRequest;
import com.example.democrm.request.role.FilterRoleRequest;
import com.example.democrm.request.role.UpdateRoleRequest;
import com.example.democrm.service.RoleService;
import com.example.democrm.utils.MyUtils;
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
@RequestMapping("/role")
public class RoleController extends BaseController {
    private final RoleService roleService;
    private final ModelMapper modelMapper = new ModelMapper();

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/all")
    ResponseEntity<?> getAllRole() {
        try {
            List<RoleDTO> response = roleService.getAll();
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getById(@PathVariable Long id) throws Exception {
        try {
            RoleDTO response = roleService.getById(id);
            return buildItemResponse(response);
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @PostMapping("")
    ResponseEntity<?> creatRole(@Validated @RequestBody CreateRoleRequest request) {
        try {
            RoleDTO response = roleService.createRole(request);
            return buildItemResponse(response);
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @PutMapping("/{id}")
    ResponseEntity<?> update(@Validated @RequestBody UpdateRoleRequest request,
                             @PathVariable("id") Long id) {
        try {
            RoleDTO response = roleService.update(request, id);
            return buildItemResponse(response);
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            RoleDTO response = roleService.deleteById(id);
            return buildItemResponse(response);
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @DeleteMapping("/delete/all")
    ResponseEntity<?> deleteAllId(@RequestBody List<Long> ids) {
        List<RoleDTO> response = roleService.deleteAllId(ids);
        return buildListItemResponse(response, response.size());
    }

    @PostMapping("/filter")
    ResponseEntity<?> filterRole(@Validated @RequestBody FilterRoleRequest request) throws ParseException {
        Page<Role> rolePage = roleService.filterRole(
                request,
                !Strings.isEmpty(request.getDateFrom()) ? MyUtils.convertDateFromString(request.getDateFrom(), DateTimeConstant.DATE_FORMAT) : null,
                !Strings.isEmpty(request.getDateTo()) ? MyUtils.convertDateFromString(request.getDateTo(), DateTimeConstant.DATE_FORMAT) : null
        );
        List<RoleDTO> roleDTOS = rolePage.getContent().stream().map(
                role -> modelMapper.map(role, RoleDTO.class)
        ).collect(Collectors.toList());
        return buildListItemResponse(roleDTOS, rolePage.getTotalElements());
    }
}