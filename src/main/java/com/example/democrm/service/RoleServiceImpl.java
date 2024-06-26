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
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

    @Autowired
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
    public RoleDTO getById(String idStr) {
        Long id;
        try {
            id = Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Tham số truyền vào không hợp lệ");
        }
        Optional<Role> role = roleRepository.findById(id);
        if (role.isPresent()) {
            return modelMapper.map(role.get(), RoleDTO.class);
        } else {
            throw new RuntimeException("Id vai tro nguời dùng không tồn tại trong hệ thống!");
        }
    }

    @Override
    public RoleDTO createRole(CreateRoleRequest request) {
        try {
            checkPermissionIsValid(request.getPermissionIds());
            Role role = Role.builder()
                    .roleName(request.getRoleName())
                    .createdDate(new Timestamp(System.currentTimeMillis()))
                    .updateDate(new Timestamp(System.currentTimeMillis()))
                    .status(request.getStatus())
                    .descriptionRole(request.getDescriptionRole())
                    .build();
            List<Permission> permissions = buildPermission(request.getPermissionIds());
            role.setPermissions(permissions);

            role = roleRepository.saveAndFlush(role);
            return modelMapper.map(role, RoleDTO.class);
        } catch (Exception ex) {
            throw new RuntimeException("Có lỗi xảy ra trong quá trình tạo vai trò mới!");
        }
    }

    @Override
    public RoleDTO update(UpdateRoleRequest request, Long id) {
        checkPermissionIsValid(request.getPermissionIds());
        validateRoleExist(id);
        Optional<Role> roleOptional = roleRepository.findById(id);
        if (roleOptional.isPresent()) {
            List<Permission> permissions = buildPermission(request.getPermissionIds());
            Role role = roleOptional.get();
            role.setRoleName(request.getRoleName());
            role.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            role.setStatus(request.getStatus());
            role.setPermissions(permissions);
            role.setDescriptionRole(request.getDescriptionRole());
            return modelMapper.map(roleRepository.save(role), RoleDTO.class);
        }
        throw new RuntimeException("Có lỗi xảy ra trong quá trình cập nhật thông tin người dùng");
    }


    //xem lai phan xoa vi no se ko map duoc doi tuong chua colection , sua lai nhu file demoCRM tra ve true false
    @Override
    public RoleDTO deleteById(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new EntityNotFoundException("Vai tro co id:" + id + " cần xóa không tồn tại trên hệ thống!");
        }
        Optional<Role> roleOptional = roleRepository.findById(id);
        if (roleOptional.isPresent()) {
            roleRepository.deleteById(id);
            return modelMapper.map(roleOptional, RoleDTO.class);
        }
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

    // ---------
    private void checkPermissionIsValid(List<String> permissionIds) {
        List<Permission> permissions = buildPermission(permissionIds);
        if (CollectionUtils.isEmpty(permissions)) {
            throw new RuntimeException("Permission không tồn tại");
        }
        List<String> listIdExists = permissions.stream().map(Permission::getPermissionId).collect(Collectors.toList());
        List<String> idNotExists = permissionIds.stream().filter(s -> !listIdExists.contains(s)).collect(Collectors.toList());
        if (!idNotExists.isEmpty())
            throw new RuntimeException(String.format("Trong danh sách permision ids có mã không tồn tại trên hệ thống: %s", idNotExists));
    }

    private void validateRoleExist(Long id) {
        boolean isExist = roleRepository.existsById(id);
        if (!isExist)
            throw new RuntimeException("Vai trò không tồn tại trên hệ thống");
    }
}
