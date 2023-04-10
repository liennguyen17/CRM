package com.example.democrm.service;

import com.example.democrm.dto.PermissionDTO;
import com.example.democrm.etity.Permission;
import com.example.democrm.request.permission.CreatePermissionRequest;
import com.example.democrm.request.permission.FilterPermissionRequest;
import com.example.democrm.request.permission.UpdatePermissionRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PermissionService {
    List<PermissionDTO> getAll();

    PermissionDTO getById(String id) throws Exception;

    PermissionDTO create(CreatePermissionRequest request);

    PermissionDTO update(UpdatePermissionRequest request, String id);

    PermissionDTO deleteById(String id);

    List<PermissionDTO> deleteAllId(List<String> ids);

    Page<Permission> filter(FilterPermissionRequest request);
}
