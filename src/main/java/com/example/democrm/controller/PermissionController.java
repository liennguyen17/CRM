package com.example.democrm.controller;

import com.example.democrm.dto.PermissionDTO;
import com.example.democrm.etity.Permission;
import com.example.democrm.request.permission.CreatePermissionRequest;
import com.example.democrm.request.permission.FilterPermissionRequest;
import com.example.democrm.request.permission.UpdatePermissionRequest;
import com.example.democrm.service.PermissionService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/permission")
public class PermissionController extends BaseController {
    private final PermissionService permissionService;
    private final ModelMapper modelMapper = new ModelMapper();

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping("/all")
    ResponseEntity<?> getAll() {
        try {
            List<PermissionDTO> response = permissionService.getAll();
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getById(@PathVariable String id) {
        try {
            PermissionDTO response = permissionService.getById(id);
            return buildItemResponse(response);
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @PostMapping("")
    ResponseEntity<?> creat(@Valid @RequestBody CreatePermissionRequest request) {
        try {
            PermissionDTO response = permissionService.create(request);
            return buildItemResponse(response);
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @PutMapping("/{id}")
    ResponseEntity<?> update(@Valid @RequestBody UpdatePermissionRequest request,
                             @PathVariable("id") String id) {
        try {
            PermissionDTO response = permissionService.update(request, id);
            return buildItemResponse(response);
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteById(@PathVariable String id) {
        try {
            PermissionDTO response = permissionService.deleteById(id);
            return buildItemResponse(response);
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @DeleteMapping("/delete/all")
    ResponseEntity<?> deleteAll(@RequestBody List<String> ids) {
        List<PermissionDTO> response = permissionService.deleteAllId(ids);
        return buildListItemResponse(response, response.size());
    }

    @PostMapping("/filter")
    ResponseEntity<?> filter(@Valid @RequestBody FilterPermissionRequest request) {
        Page<Permission> permissionPage = permissionService.filter(request);
        List<PermissionDTO> permissionDTOS = permissionPage.getContent().stream().map(
                permission -> modelMapper.map(permission, PermissionDTO.class)
        ).collect(Collectors.toList());
        return buildListItemResponse(permissionDTOS, permissionPage.getTotalElements());
    }
}