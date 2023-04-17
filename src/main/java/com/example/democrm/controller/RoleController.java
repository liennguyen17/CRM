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
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    ResponseEntity<?> getAllRole() {
        try {
            List<RoleDTO> response = roleService.getAll();
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    ResponseEntity<?> getById(@PathVariable("id") Long id) throws Exception {
        try {
            RoleDTO response = roleService.getById(id);
            return buildItemResponse(response);
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('ADMIN','CREATE_ROLE')")
    ResponseEntity<?> creatRole(@Validated @RequestBody CreateRoleRequest request) {
        try {
            RoleDTO response = roleService.createRole(request);
            return buildItemResponse(response);
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','UPDATE_ROLE')")
    ResponseEntity<?> update(@Validated @RequestBody UpdateRoleRequest request,
                             @PathVariable("id") Long id) {
        try {
            RoleDTO response = roleService.update(request, id);
            return buildItemResponse(response);
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    //xem lai khi van xoa duoc nhung van nhay vao loi _ tinh sua lai nhu sau
    //xem lai phan xoa vi no se ko map duoc doi tuong chua colection , sua lai nhu file demoCRM tra ve true false
    /*
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteById(@PathVariable Long id) {
    RoleDTO response = roleService.deleteById(id);
            return buildItemResponse(response);
        }
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DELETE_ROLE')")
    ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        try {
            RoleDTO response = roleService.deleteById(id);
            return buildItemResponse(response);
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @DeleteMapping("/delete/all")
    @PreAuthorize("hasAnyAuthority('ADMIN','DELETE_ROLE')")
    ResponseEntity<?> deleteAllId(@RequestBody List<Long> ids) {
        List<RoleDTO> response = roleService.deleteAllId(ids);
        return buildListItemResponse(response, response.size());
    }

    @PostMapping("/filter")
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
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
