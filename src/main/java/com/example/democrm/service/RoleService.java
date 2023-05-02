package com.example.democrm.service;

import com.example.democrm.dto.RoleDTO;
import com.example.democrm.etity.Role;
import com.example.democrm.request.role.CreateRoleRequest;
import com.example.democrm.request.role.FilterRoleRequest;
import com.example.democrm.request.role.UpdateRoleRequest;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface RoleService {
    List<RoleDTO> getAll();

    RoleDTO getById(String idStr) throws Exception;

    RoleDTO createRole(CreateRoleRequest request);

    RoleDTO update(UpdateRoleRequest request, Long id);

    RoleDTO deleteById(Long id);

    List<RoleDTO> deleteAllId(List<Long> ids);

    Page<Role> filterRole(FilterRoleRequest request, Date dateFrom, Date dateTo);
}
