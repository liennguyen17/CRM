package com.example.democrm.service;

import com.example.democrm.constant.DateTimeConstant;
import com.example.democrm.dto.UserDTO;
import com.example.democrm.etity.Role;
import com.example.democrm.etity.User;
import com.example.democrm.repository.CustomUserRepository;
import com.example.democrm.repository.RoleRepository;
import com.example.democrm.repository.UserRepository;
import com.example.democrm.request.user.CreateUserRequest;
import com.example.democrm.request.user.FilterUserRequest;
import com.example.democrm.request.user.UpdateUserRequest;
import com.example.democrm.utils.MyUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    @Override
    public List<UserDTO> getAll() {
        return userRepository.findAll().stream().map(
                user -> modelMapper.map(user, UserDTO.class)
        ).collect(Collectors.toList());
    }

    @Override
    public UserDTO getById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return modelMapper.map(user.get(), UserDTO.class);
        } else {
            throw new RuntimeException("Id nguời dùng không tồn tại");
        }
    }

    @Override
    @Transactional
    public UserDTO createUser(CreateUserRequest request) throws ParseException {
        checkUserIsExistByName(request.getUserName(), null);
        checkRoleIsValid(request.getRoleId());
        Optional<Role> roleOptional = roleRepository.findById(request.getRoleId());
        User user = User.builder()
                .userName(request.getUserName())
                .date(MyUtils.convertDateFromString(request.getDate(), DateTimeConstant.DATE_FORMAT))
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .address(request.getAddress())
                .isSuperAdmin(false)
                .createdDate(new Timestamp(System.currentTimeMillis()))
                .updateDate(new Timestamp(System.currentTimeMillis()))
                .build();
        user.setRole(buildRole(roleOptional.get().getRoleId()));    //lay ra id cua role_sau r se thu xem get dc name ko
        user = userRepository.saveAndFlush(user);
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    @Transactional
    public UserDTO update(UpdateUserRequest request, Long id) throws ParseException {
        checkUserIsExistByName(request.getUserName(), request.getId());
        checkRoleIsValid(request.getRoleId());
        validateUserExist(request.getId());
        Optional<User> userOptional = userRepository.findById(id);
        Optional<Role> roleOptional = roleRepository.findById(request.getRoleId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUserName(request.getUserName());
            user.setDate(MyUtils.convertDateFromString(request.getDate(), DateTimeConstant.DATE_FORMAT));
            user.setEmail(request.getEmail());
            user.setPassword(encoder.encode(request.getPassword()));
            user.setAddress(request.getAddress());
            user.setIsSuperAdmin(request.getIsSuperAdmin());
            user.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            user.setRole(buildRole(roleOptional.get().getRoleId()));
            return modelMapper.map(userRepository.save(user), UserDTO.class);
        }
        throw new RuntimeException("Có lỗi xảy ra trong quá trình cập nhật thông tin người dùng");
    }

    @Override
    @Transactional
    public UserDTO deleteById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
            return modelMapper.map(userOptional, UserDTO.class);
        }
//        return null;
        throw new RuntimeException("Có lỗi xảy ra trong quá trình xóa người dùng");
    }

    @Override
    @Transactional
    public List<UserDTO> deleteAllId(List<Long> ids) {
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user : userRepository.findAllById(ids)) {
            userDTOS.add(modelMapper.map(user, UserDTO.class));
        }
        userRepository.deleteAllByIdInBatch(ids);
        return userDTOS;
    }

    @Override
    public Page<User> filterUser(FilterUserRequest request, Date dateFrom, Date dateTo) {
        Specification<User> specification = CustomUserRepository.filterSpecification(dateFrom, dateTo, request);
        Page<User> userPage = userRepository.findAll(specification, PageRequest.of(request.getStart(), request.getLimit()));
        return userPage;
    }


    private void checkUserIsExistByName(String name, Long id) {
        if (id == null && userRepository.existsAllByUserName(name))
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        if (id != null && userRepository.existsAllByUserNameAndUserIdNot(name, id))
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
    }

    private void validateUserExist(Long id) {
        boolean isExist = userRepository.existsById(id);
        if (!isExist) {
            throw new RuntimeException("Người dùng không tồn tại trên hệ thống");
        }
    }

    private List<Role> buildRole(List<Long> roleIds) {
        return CollectionUtils.isEmpty(roleIds) ? new ArrayList<>() : roleRepository.findAllById(roleIds);
    }

    private void checkRoleIsValid(Long roleId) {
        if (roleId == null)
            return;
        Role role = buildRole(roleId);
        if (role == null) {
            throw new RuntimeException("Role không tồn tại");
        }
    }

    private Role buildRole(Long roleId) {
        return roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role không tồn tại"));
    }
}
