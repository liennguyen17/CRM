package com.example.democrm.service;

import com.example.democrm.dto.PermissionDTO;
import com.example.democrm.etity.Permission;
import com.example.democrm.repository.CustomPermissionRepository;
import com.example.democrm.repository.PermissionRepository;
import com.example.democrm.request.permission.CreatePermissionRequest;
import com.example.democrm.request.permission.FilterPermissionRequest;
import com.example.democrm.request.permission.UpdatePermissionRequest;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public List<PermissionDTO> getAll() {
        return permissionRepository.findAll().stream().map(
                permission -> modelMapper.map(permission, PermissionDTO.class)
        ).collect(Collectors.toList());
    }

    @Override
    public PermissionDTO getById(String id) throws Exception {
        Optional<Permission> permissionOptional = permissionRepository.findById(id);
        if (permissionOptional.isPresent()) {
            return modelMapper.map(permissionOptional.get(), PermissionDTO.class);
        } else {
            throw new Exception("Id nguời dùng không tồn tại");
        }
//        return null;
    }

    @Override
    public PermissionDTO create(CreatePermissionRequest request) {
        Permission permission = Permission.builder()
                .permissionId(request.getPermissionId())
                .permissionName(request.getPermissionName())
                .description(request.getDescription())
                .status(request.getStatus())
                .build();
        permission = permissionRepository.saveAndFlush(permission);
        return modelMapper.map(permission, PermissionDTO.class);
    }

    @Override
    public PermissionDTO update(UpdatePermissionRequest request, String id) {
        Optional<Permission> permissionOptional = permissionRepository.findById(id);
        if (permissionOptional.isPresent()) {
            Permission permission = permissionOptional.get();
            permission.setPermissionName(request.getPermissionName());
            permission.setDescription(request.getDescription());
            permission.setStatus(request.getStatus());
            return modelMapper.map(permissionRepository.save(permission), PermissionDTO.class);
        }
        throw new RuntimeException("Có lỗi xảy ra trong quá trình cập nhật thông tin người dùng");
    }

    @Override
    public PermissionDTO deleteById(String id) {
        Optional<Permission> permissionOptional = permissionRepository.findById(id);
        if (permissionOptional.isPresent()) {
            permissionRepository.deleteById(id);
            return modelMapper.map(permissionOptional, PermissionDTO.class);
        }
        throw new RuntimeException("Có lỗi xảy ra trong quá trình xóa người dùng");
    }

    @Override
    public List<PermissionDTO> deleteAllId(List<String> ids) {
        List<PermissionDTO> permissionDTOS = new ArrayList<>();
        for (Permission permission : permissionRepository.findAllById(ids)) {
            permissionDTOS.add(modelMapper.map(permission, PermissionDTO.class));
        }
        permissionRepository.deleteAllByIdInBatch(ids);
        return permissionDTOS;
    }

    @Override
    public Page<Permission> filter(FilterPermissionRequest request) {
        Specification<Permission> specification = CustomPermissionRepository.filterSpecification(request);
        Page<Permission> permissionPage = permissionRepository.findAll(specification, PageRequest.of(request.getStart(), request.getLimit()));
        return permissionPage;
    }
}
