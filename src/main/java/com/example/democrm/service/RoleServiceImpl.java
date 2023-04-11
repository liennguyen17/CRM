package com.example.democrm.service;

import com.example.democrm.dto.RoleDTO;
import com.example.democrm.etity.Permission;
import com.example.democrm.etity.Role;
import com.example.democrm.repository.CustomRoleRepository;
import com.example.democrm.repository.PermissionRepository;
import com.example.democrm.repository.RoleRepository;
import com.example.democrm.request.role.CreateRoleRequest;
import com.example.democrm.request.role.FilterRoleRequest;
import com.example.democrm.request.role.UpdateRoleRequest;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public RoleServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public List<RoleDTO> getAll() {
        return roleRepository.findAll().stream().map(
                role -> modelMapper.map(role, RoleDTO.class)
        ).collect(Collectors.toList());
    }

    @Override
    public RoleDTO getById(Long id) throws Exception {
        Optional<Role> role = roleRepository.findById(id);
        if (role.isPresent()) {
            return modelMapper.map(role.get(), RoleDTO.class);
        } else {
            throw new Exception("Id nguời dùng không tồn tại");
        }
//        return null;
    }

    @Override
    public RoleDTO createRole(CreateRoleRequest request) {
        Role role = Role.builder()
                .roleName(request.getRoleName())
                .createdDate(new Timestamp(System.currentTimeMillis()))
                .updateDate(new Timestamp(System.currentTimeMillis()))
                .status(request.getStatus())
                .build();
        List<Permission> permissions = buildPermission(request.getPermissionIds());
        role.setPermissions(permissions);

        role = roleRepository.saveAndFlush(role);
        return modelMapper.map(role, RoleDTO.class);
    }

    @Override
    public RoleDTO update(UpdateRoleRequest request, Long id) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        if (roleOptional.isPresent()) {
            List<Permission> permissions = buildPermission(request.getPermissionIds());
            Role role = roleOptional.get();
            role.setRoleName(request.getRoleName());
            role.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            role.setStatus(request.getStatus());
            role.setPermissions(permissions);
            return modelMapper.map(roleRepository.save(role), RoleDTO.class);
        }
        throw new RuntimeException("Có lỗi xảy ra trong quá trình cập nhật thông tin người dùng");
    }


    //xem lai phan xoa
    @Override
    public RoleDTO deleteById(Long id) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        if (roleOptional.isPresent()) {
            roleRepository.deleteById(id);
            return modelMapper.map(roleOptional, RoleDTO.class);
        }
//        return null;
        throw new RuntimeException("Có lỗi xảy ra trong quá trình xóa người dùng");
    }

    @Override
    public List<RoleDTO> deleteAllId(List<Long> ids) {
        List<RoleDTO> roleDTOS = new ArrayList<>();
        for (Role role : roleRepository.findAllById(ids)) {
            roleDTOS.add(modelMapper.map(role, RoleDTO.class));
        }
        roleRepository.deleteAllByIdInBatch(ids);
        return roleDTOS;
    }

    @Override
    public Page<Role> filterRole(FilterRoleRequest request, Date dateFrom, Date dateTo) {
        Specification<Role> specification = CustomRoleRepository.filterSpecification(dateFrom, dateTo, request);
        Page<Role> rolePage = roleRepository.findAll(specification, PageRequest.of(request.getStart(), request.getLimit()));
        return rolePage;
    }

    private List<Permission> buildPermission(List<String> permissionIds) {
        return permissionRepository.findAllById(permissionIds);
    }
}
