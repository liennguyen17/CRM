package com.example.democrm.service;

import com.example.democrm.constant.DateTimeConstant;
import com.example.democrm.dto.CustomerGroupDTO;
import com.example.democrm.dto.CustomersDTO;
import com.example.democrm.dto.UserDTO;
import com.example.democrm.etity.CustomerGroup;
import com.example.democrm.etity.Customers;
import com.example.democrm.etity.Role;
import com.example.democrm.etity.User;
import com.example.democrm.repository.*;
import com.example.democrm.request.user.CreateUserRequest;
import com.example.democrm.request.user.FilterUserRequest;
import com.example.democrm.request.user.UpdateUserRequest;
import com.example.democrm.utils.MyUtils;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final CustomerGroupRepository customerGroupRepository;
    private final CustomersRepository customersRepository;
    private final PasswordEncoder encoder;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, CustomerGroupRepository customerGroupRepository, CustomersRepository customersRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.customerGroupRepository = customerGroupRepository;
        this.customersRepository = customersRepository;
        this.encoder = encoder;
    }

    @Override
    public List<UserDTO> getAll() {
        return userRepository.findAll().stream().map(
                user -> modelMapper.map(user, UserDTO.class)
        ).collect(Collectors.toList());
    }

    @Override
    public UserDTO getById(String idStr) {
        Long id;
        try {
            id = Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Tham số truyền vào không hợp lệ");
        }
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return modelMapper.map(user.get(), UserDTO.class);
        } else {
            throw new RuntimeException("Id nguời dùng không tồn tại trong hệ thống!");
        }
    }

    @Override
    @Transactional
    public UserDTO createUser(CreateUserRequest request) throws ParseException {
        try {
            checkUserIsExistByName(request.getUserName(), null);
            checkRoleIsValid(request.getRoleId());
            Optional<Role> roleOptional = roleRepository.findById(request.getRoleId());
            User user = User.builder()
                    .userName(request.getUserName())
                    .name(request.getName())
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
        } catch (Exception ex) {
            throw new RuntimeException("Có lỗi xảy ra trong quá trình tạo người dùng mới");
        }
    }

    @Override
    @Transactional
    public UserDTO update(UpdateUserRequest request, Long id) throws ParseException {
        checkUserIsExistByName(request.getUserName(), id);
        checkRoleIsValid(request.getRoleId());
        validateUserExist(id);
        Optional<User> userOptional = userRepository.findById(id);
        Optional<Role> roleOptional = roleRepository.findById(request.getRoleId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUserName(request.getUserName());
            user.setName(request.getName());
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
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Người dùng có id:" + id + " cần xóa không tồn tại trong hệ thống!");
        }
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
            return modelMapper.map(userOptional, UserDTO.class);
        }
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
    public Page<User> filterUser(FilterUserRequest request, Date dateFrom, Date dateTo, Date dateOfBirthFrom, Date dateOfBirthTo) {
        Specification<User> specification = CustomUserRepository.filterSpecification(dateFrom, dateTo, dateOfBirthFrom, dateOfBirthTo, request);
        Page<User> userPage = userRepository.findAll(specification, PageRequest.of(request.getStart(), request.getLimit()));
        return userPage;
    }

    //trang admin
    //Lấy danh sách khách hàng ứng với nhân viên trong trang admin
    @Override
    public List<CustomersDTO> getUserManagedCustomersPageAdmin(Long id) {
        try{
            List<Customers> customersList = customersRepository.findAllByUser_UserId(id);
            List<CustomersDTO> customersDTOS = customersList.stream()
                    .map(customers -> modelMapper.map(customers, CustomersDTO.class))
                    .collect(Collectors.toList());
            return customersDTOS;
        }catch (Exception ex){
            throw new RuntimeException("Không tìm thấy danh sách khách hàng của nhân viên có mã là: " +id+ " ");
        }
    }

    //thống kê user(là nhân viên) quản lý những nhóm nào hiển thị trong trang admin
    @Override
    public List<CustomerGroupDTO> getUserManagedCustomerGroupsPageAdmin(Long id) {
        try{
            List<CustomerGroup> managedCustomerGroups = customerGroupRepository.getAllByUser_UserId(id);

            List<CustomerGroupDTO> customerGroupDTOS = managedCustomerGroups.stream()
                    .map(customerGroup -> modelMapper.map(customerGroup, CustomerGroupDTO.class))
                    .collect(Collectors.toList());
            return customerGroupDTOS;
        }catch (Exception ex){
            throw new RuntimeException("Không tìm thấy danh sách nhóm khách hàng của nhân viên có mã là: " +id+ " ");
        }
    }



    //trang nhân viên
    //thống kê user quản lý những nhóm nào
    @Override
    public List<CustomerGroupDTO> getUserManagedCustomerGroups() {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String loggedInUsername = authentication.getName();

            User loggedUsers = userRepository.getUserByUserName(loggedInUsername);

            List<CustomerGroup> managedCustomerGroups = customerGroupRepository.getAllByUser_UserId(loggedUsers.getUserId());

            List<CustomerGroupDTO> customerGroupDTOS = managedCustomerGroups.stream()
                    .map(customerGroup -> modelMapper.map(customerGroup, CustomerGroupDTO.class))
                    .collect(Collectors.toList());
            return customerGroupDTOS;
        }catch (Exception ex){
            throw new RuntimeException("Không tìm thấy thông tin người dùng. Vui lòng đăng nhập lại.");
        }
    }

    //thống kê user quản lý số khách hàng thuộc nhóm đang quản lý
    @Override
    public List<CustomersDTO> getUserManagedCustomer() {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String loggedInUsername = authentication.getName();

            User loggedUsers = userRepository.getUserByUserName(loggedInUsername);

            //lấy ra các nhóm khách hàng có id do user đó quản lý
            List<CustomerGroup> managedCustomerGroups = customerGroupRepository.getAllByUser_UserId(loggedUsers.getUserId());

            List<Customers> customers = new ArrayList<>();

            //từ danh sách các nhóm lấy ra các khách hàng thuộc nhóm trên
            for (CustomerGroup group : managedCustomerGroups) {
                List<Customers> managerCustomer = customersRepository.findAllByCustomerGroup_CustomerGroupId(group.getCustomerGroupId());
                customers.addAll(managerCustomer);
            }

            List<CustomersDTO> customersDTOList = customers.stream()
                    .map(managedCustomer -> modelMapper.map(managedCustomer, CustomersDTO.class))
                    .collect(Collectors.toList());
            return customersDTOList;
        }catch (Exception ex){
            throw new RuntimeException("Không tìm thấy thông tin người dùng. Vui lòng đăng nhập lại.");
        }

    }

    //thống kê nhân viên(user) quản lý khách hàng của nhân viên đó
    @Override
    public List<CustomersDTO> getUserManagedCustomers(){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String loggedInUsername = authentication.getName();
            User loggedUsers = userRepository.getUserByUserName(loggedInUsername);
            //lấy ra khách hàng do nhân viên đó quản lý
            List<Customers> managedCustomers = customersRepository.findAllByUser_UserId(loggedUsers.getUserId());
            List<CustomersDTO> customersDTOList = managedCustomers.stream()
                    .map(customers -> modelMapper.map(customers, CustomersDTO.class))
                    .collect(Collectors.toList());
            return customersDTOList;
        }catch(Exception ex){
            throw new RuntimeException("Không tìm thấy thông tin người dùng. Vui lòng đăng nhập lại.");
        }


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
