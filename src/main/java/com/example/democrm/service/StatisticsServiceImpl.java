package com.example.democrm.service;

import com.example.democrm.constant.DateTimeConstant;
import com.example.democrm.dto.StatisticsCustomerStatusDTO;
import com.example.democrm.dto.StatisticsStaffDTO;
import com.example.democrm.etity.CustomerStatus;
import com.example.democrm.etity.Customers;
import com.example.democrm.etity.User;
import com.example.democrm.repository.CustomerGroupRepository;
import com.example.democrm.repository.CustomerStatusRepository;
import com.example.democrm.repository.CustomersRepository;
import com.example.democrm.repository.UserRepository;
import com.example.democrm.request.statistic.StatisticCustomerAndStaffRequest;
import com.example.democrm.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    private final UserRepository userRepository;
    private final CustomersRepository customersRepository;
    private final CustomerGroupRepository customerGroupRepository;
    private final CustomerStatusRepository customerStatusRepository;

    @Autowired
    public StatisticsServiceImpl(UserRepository userRepository, CustomersRepository customersRepository, CustomerGroupRepository customerGroupRepository, CustomerStatusRepository customerStatusRepository) {
        this.userRepository = userRepository;
        this.customersRepository = customersRepository;
        this.customerGroupRepository = customerGroupRepository;
        this.customerStatusRepository = customerStatusRepository;
    }


    public List<StatisticsCustomerStatusDTO> getCustomerStatusList() {
        List<StatisticsCustomerStatusDTO> customerStatusDTOS = new ArrayList<>();
        List<CustomerStatus> statusList = customerStatusRepository.findAll();

        //xác định khoảng thời gian trong tháng
        LocalDate now = LocalDate.now();

        //th1: khi thống kê sẽ lấy tất cả các tháng
//        LocalDate startDate = now.withDayOfMonth(1);//ngày đầu tiên của tháng
//        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());//ngày cuối cùng của tháng

        //th2: khi thống kê chỉ lấy tháng hiện tại
//        LocalDate startDate = now.with(TemporalAdjusters.firstDayOfMonth());
//        LocalDate endDate = now.with(TemporalAdjusters.lastDayOfMonth());

        //giống th2
        LocalDate startDate = LocalDate.of(now.getYear(), now.getMonth(), 1); // Ngày đầu tiên của tháng hiện tại
        LocalDate endDate = now.with(TemporalAdjusters.lastDayOfMonth()); // Ngày cuối cùng của tháng hiện tại

        for (CustomerStatus status : statusList) {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

            Timestamp startTimestamp = Timestamp.valueOf(startDateTime);
            Timestamp endTimestamp = Timestamp.valueOf(endDateTime);

            Long count = customersRepository.countByCustomerStatusAndCreatedDateBetween(status, startTimestamp, endTimestamp);
            StatisticsCustomerStatusDTO dto = new StatisticsCustomerStatusDTO(status.getStatusName(), count.intValue());
            customerStatusDTOS.add(dto);
        }
        return customerStatusDTOS;
    }


    //thống kê trạng thái khách hàng trong tháng hiện tại
    @Override
    public List<StatisticsCustomerStatusDTO> getCustomerStatusListPresentMonth() {
        List<StatisticsCustomerStatusDTO> customerStatusDTOS = new ArrayList<>();
        List<CustomerStatus> statusList = customerStatusRepository.findAll();

        //xác định khoảng thời gian trong tháng
        LocalDate now = LocalDate.now();
        LocalDate startDate = LocalDate.of(now.getYear(), now.getMonth(), 1); // Ngày đầu tiên của tháng hiện tại
        LocalDate endDate = now.with(TemporalAdjusters.lastDayOfMonth()); // Ngày cuối cùng của tháng hiện tại

        for (CustomerStatus status : statusList) {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

            Timestamp startTimestamp = Timestamp.valueOf(startDateTime);
            Timestamp endTimestamp = Timestamp.valueOf(endDateTime);

            Long count = customersRepository.countByCustomerStatusAndCreatedDateBetween(status, startTimestamp, endTimestamp);
            StatisticsCustomerStatusDTO dto = new StatisticsCustomerStatusDTO(status.getStatusName(), count.intValue());
            customerStatusDTOS.add(dto);
        }
        return customerStatusDTOS;
    }


    //thống kê trạng thái khách hàng trong tháng trước tháng hiện tại
    @Override
    public List<StatisticsCustomerStatusDTO> getCustomerStatusListPreviousMonth() {
        List<StatisticsCustomerStatusDTO> customerStatusDTOS = new ArrayList<>();
        List<CustomerStatus> statusList = customerStatusRepository.findAll();

        LocalDate now = LocalDate.now();
        LocalDate startDate = now.with(TemporalAdjusters.firstDayOfMonth()).minusMonths(1); // Ngày đầu tiên của tháng trước
        LocalDate endDate = now.with(TemporalAdjusters.lastDayOfMonth()).minusMonths(1); // Ngày cuối cùng của tháng trước

        for (CustomerStatus status : statusList) {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

            Timestamp startTimestamp = Timestamp.valueOf(startDateTime);
            Timestamp endTimestamp = Timestamp.valueOf(endDateTime);

            Long count = customersRepository.countByCustomerStatusAndCreatedDateBetween(status, startTimestamp, endTimestamp);
            StatisticsCustomerStatusDTO dto = new StatisticsCustomerStatusDTO(status.getStatusName(), count.intValue());
            customerStatusDTOS.add(dto);
        }
        return customerStatusDTOS;
    }


    //thống kê mỗi nhân viên quản lý được bao nhiêu khách hàng có trạng thái thành công theo tuần
    @Override
    public List<StatisticsStaffDTO> getAllStatisticStaffByWeek(){
        List<StatisticsStaffDTO> staffDTOList = new ArrayList<>();

        //xác định khoảng thời gian ngày bắt đầu và ngày kết thúc trong tuần
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        //lấy ra danh sách user là nhân viên
        List<User> userList = userRepository.findAllByIsSuperAdminFalse();

        for(User user : userList){
            //Lấy danh sách khách hàng thuộc user và có trạng thái là "thành công"
            List<Customers> successfulCustomers = customersRepository.findByUserAndCustomerStatus_CustomerStatusId(user,2L);

            //Lọc danh sách khách hàng nằm trong tuần hiện tại
            List<Customers> weeklySuccessfulCustomers = successfulCustomers.stream()
                    .filter(customers -> {
//                        LocalDateTime createdDateTime = customers.getCreatedDate().toLocalDateTime();
                        LocalDateTime updateDateTime = customers.getUpdateDate().toLocalDateTime();
                        return updateDateTime.isAfter(startOfWeek.atStartOfDay()) && updateDateTime.isBefore(endOfWeek.plusDays(1).atStartOfDay());
                    }).collect(Collectors.toList());
            int salesCount = weeklySuccessfulCustomers.size();
            StatisticsStaffDTO staffDTO = new StatisticsStaffDTO(user.getUserName(),salesCount);
            staffDTOList.add(staffDTO);
        }
        return staffDTOList;
    }


    //thống kê mỗi nhân viên quản lý được bao nhiêu khách hàng có trạng thái chưa xử lý theo tuần
    @Override
    public List<StatisticsStaffDTO> getAllStatisticStaffByWeek1() {
        List<StatisticsStaffDTO> staffDTOList = new ArrayList<>();

        //xác định khoảng thời gian ngày bắt đầu và ngày kết thúc trong tuần
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        //lấy ra danh sách user là nhân viên
        List<User> userList = userRepository.findAllByIsSuperAdminFalse();

        for(User user : userList){
            //Lấy danh sách khách hàng thuộc user và có trạng thái là "thành công"
            List<Customers> successfulCustomers = customersRepository.findByUserAndCustomerStatus_CustomerStatusId(user,4L);

            //Lọc danh sách khách hàng nằm trong tuần hiện tại
            List<Customers> weeklySuccessfulCustomers = successfulCustomers.stream()
                    .filter(customers -> {
//                        LocalDateTime createdDateTime = customers.getCreatedDate().toLocalDateTime();
                        LocalDateTime updateDateTime = customers.getUpdateDate().toLocalDateTime();
                        return updateDateTime.isAfter(startOfWeek.atStartOfDay()) && updateDateTime.isBefore(endOfWeek.plusDays(1).atStartOfDay());
                    }).collect(Collectors.toList());
            int salesCount = weeklySuccessfulCustomers.size();
            StatisticsStaffDTO staffDTO = new StatisticsStaffDTO(user.getUserName(),salesCount);
            staffDTOList.add(staffDTO);
        }
        return staffDTOList;
    }

    //trang staff
    //thống kê tất cả các khách hàng theo trạng thái của tháng hiện tại ung voi nhan vien cụ the
    @Override
    public List<StatisticsCustomerStatusDTO> getCustomerStatusListPresentMonthByStaff() {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String loggedInUsername = authentication.getName();
            User loggedUser = userRepository.getUserByUserName(loggedInUsername);

            // Lấy ra khách hàng do nhân viên đó quản lý
            List<Customers> managedCustomers = customersRepository.findAllByUser_UserId(loggedUser.getUserId());

            List<StatisticsCustomerStatusDTO> customerStatusDTOS = new ArrayList<>();
            List<CustomerStatus> statusList = customerStatusRepository.findAll();

            // Xác định khoảng thời gian trong tháng
            LocalDate now = LocalDate.now();
            LocalDate startDate = LocalDate.of(now.getYear(), now.getMonth(), 1); // Ngày đầu tiên của tháng hiện tại
            LocalDate endDate = now.with(TemporalAdjusters.lastDayOfMonth()); // Ngày cuối cùng của tháng hiện tại

            for (CustomerStatus status : statusList) {
                LocalDateTime startDateTime = startDate.atStartOfDay();
                LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

                Timestamp startTimestamp = Timestamp.valueOf(startDateTime);
                Timestamp endTimestamp = Timestamp.valueOf(endDateTime);

    //            long count =0;
                long count = managedCustomers.stream()
                        .filter(customer -> customer.getCustomerStatus().equals(status) &&
                                customer.getUpdateDate().after(startTimestamp) &&
                                customer.getUpdateDate().before(endTimestamp))
                        .count();

                StatisticsCustomerStatusDTO dto = new StatisticsCustomerStatusDTO(status.getStatusName(), (int) count);
                customerStatusDTOS.add(dto);
            }

            return customerStatusDTOS;
        }catch(Exception ex){
            throw new RuntimeException("Không tìm thấy thông tin người dùng. Vui lòng đăng nhập lại.");
        }

    }

    //thống kê truyền tự động
    //trang admin
    //thống kê trạng thái khách hàng
    @Override
    public List<StatisticsCustomerStatusDTO> getCustomerStatusList(StatisticCustomerAndStaffRequest request) throws ParseException {

        List<StatisticsCustomerStatusDTO> customerStatusDTOS = new ArrayList<>();
        List<CustomerStatus> statusList = customerStatusRepository.findAll();

        Date dateFromString = MyUtils.convertDateFromString(request.getDateFrom(), DateTimeConstant.DATE_FORMAT);
        Date dateToString = MyUtils.convertDateFromString(request.getDateTo(), DateTimeConstant.DATE_FORMAT);

        for (CustomerStatus status : statusList) {

            Timestamp timestampFrom = new Timestamp(dateFromString.getTime());
            Timestamp timestampTo = new Timestamp(dateToString.getTime());

            Long count = customersRepository.countByCustomerStatusAndCreatedDateBetween(status, timestampFrom, timestampTo);
            StatisticsCustomerStatusDTO dto = new StatisticsCustomerStatusDTO(status.getStatusName(), count.intValue());
            customerStatusDTOS.add(dto);
        }
        return customerStatusDTOS;
    }

    //thống kê mỗi nhân viên quản lý được bao nhiêu khách hàng có trạng thái thành công theo khoảng thời gian tự truyền
    @Override
    public List<StatisticsStaffDTO> getAllStatusSuccessCustomer_Staff(StatisticCustomerAndStaffRequest request) throws ParseException {
        List<StatisticsStaffDTO> staffDTOList = new ArrayList<>();

        Date dateFromString = MyUtils.convertDateFromString(request.getDateFrom(), DateTimeConstant.DATE_FORMAT);
        Date dateToString = MyUtils.convertDateFromString(request.getDateTo(), DateTimeConstant.DATE_FORMAT);

        LocalDate localDateFrom = dateFromString.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDateTo = dateToString.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();


        //lấy ra danh sách user là nhân viên
        List<User> userList = userRepository.findAllByIsSuperAdminFalse();

        for(User user : userList){
            //Lấy danh sách khách hàng thuộc user và có trạng thái là "thành công"
            List<Customers> successfulCustomers = customersRepository.findByUserAndCustomerStatus_CustomerStatusId(user,2L);

            //Lọc danh sách khách hàng nằm trong khoảng thời gian truyền
            List<Customers> weeklySuccessfulCustomers = successfulCustomers.stream()
                    .filter(customers -> {
//                        LocalDateTime createdDateTime = customers.getCreatedDate().toLocalDateTime();
                        LocalDateTime updateDateTime = customers.getUpdateDate().toLocalDateTime();
                        return updateDateTime.isAfter(localDateFrom.atStartOfDay()) && updateDateTime.isBefore(localDateTo.plusDays(1).atStartOfDay());
                    }).collect(Collectors.toList());
            int salesCount = weeklySuccessfulCustomers.size();
            StatisticsStaffDTO staffDTO = new StatisticsStaffDTO(user.getUserName(),salesCount);
            staffDTOList.add(staffDTO);
        }
        return staffDTOList;
    }


    //thống kê mỗi nhân viên quản lý được bao nhiêu khách hàng có trạng thái "chưa xử lý" theo khoảng thời gian tự truyền
    @Override
    public List<StatisticsStaffDTO> getAllStatus4Customer_Staff(StatisticCustomerAndStaffRequest request) throws ParseException {
        List<StatisticsStaffDTO> staffDTOList = new ArrayList<>();

        Date dateFromString = MyUtils.convertDateFromString(request.getDateFrom(), DateTimeConstant.DATE_FORMAT);
        Date dateToString = MyUtils.convertDateFromString(request.getDateTo(), DateTimeConstant.DATE_FORMAT);

        LocalDate localDateFrom = dateFromString.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDateTo = dateToString.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();


        //lấy ra danh sách user là nhân viên
        List<User> userList = userRepository.findAllByIsSuperAdminFalse();

        for(User user : userList){
            //Lấy danh sách khách hàng thuộc user và có trạng thái là "thành công"
            List<Customers> successfulCustomers = customersRepository.findByUserAndCustomerStatus_CustomerStatusId(user,4L);

            //Lọc danh sách khách hàng nằm trong khoảng thời gian truyền
            List<Customers> weeklySuccessfulCustomers = successfulCustomers.stream()
                    .filter(customers -> {
//                        LocalDateTime createdDateTime = customers.getCreatedDate().toLocalDateTime();
                        LocalDateTime updateDateTime = customers.getUpdateDate().toLocalDateTime();
                        return updateDateTime.isAfter(localDateFrom.atStartOfDay()) && updateDateTime.isBefore(localDateTo.plusDays(1).atStartOfDay());
                    }).collect(Collectors.toList());
            int salesCount = weeklySuccessfulCustomers.size();
            StatisticsStaffDTO staffDTO = new StatisticsStaffDTO(user.getUserName(),salesCount);
            staffDTOList.add(staffDTO);
        }
        return staffDTOList;
    }


    //trang staff
    //thống kê tất cả các khách hàng theo trạng thái của tháng hiện tại ung voi nhan vien cụ the
    @Override
    public List<StatisticsCustomerStatusDTO> getCustomerStatusListPageStaff(StatisticCustomerAndStaffRequest request)throws ParseException {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String loggedInUsername = authentication.getName();
            User loggedUser = userRepository.getUserByUserName(loggedInUsername);

            // Lấy ra khách hàng do nhân viên đó quản lý
            List<Customers> managedCustomers = customersRepository.findAllByUser_UserId(loggedUser.getUserId());

            List<StatisticsCustomerStatusDTO> customerStatusDTOS = new ArrayList<>();
            List<CustomerStatus> statusList = customerStatusRepository.findAll();

            // Xác định khoảng thời gian trong tháng
            LocalDate now = LocalDate.now();
            LocalDate startDate = LocalDate.of(now.getYear(), now.getMonth(), 1); // Ngày đầu tiên của tháng hiện tại
            LocalDate endDate = now.with(TemporalAdjusters.lastDayOfMonth()); // Ngày cuối cùng của tháng hiện tại

            for (CustomerStatus status : statusList) {
                //
                LocalDateTime startDateTime = startDate.atStartOfDay();
                LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

                Timestamp startTimestamp = Timestamp.valueOf(startDateTime);
                Timestamp endTimestamp = Timestamp.valueOf(endDateTime);

                //

                Date dateFromString = MyUtils.convertDateFromString(request.getDateFrom(), DateTimeConstant.DATE_FORMAT);
                Date dateToString = MyUtils.convertDateFromString(request.getDateTo(), DateTimeConstant.DATE_FORMAT);

                Timestamp timestampFrom = new Timestamp(dateFromString.getTime());
                Timestamp timestampTo = new Timestamp(dateToString.getTime());


                //            long count =0;
                long count = managedCustomers.stream()
                        .filter(customer -> customer.getCustomerStatus().equals(status) &&
                                customer.getUpdateDate().after(timestampFrom) &&
                                customer.getUpdateDate().before(timestampTo))
                        .count();

                StatisticsCustomerStatusDTO dto = new StatisticsCustomerStatusDTO(status.getStatusName(), (int) count);
                customerStatusDTOS.add(dto);
            }

            return customerStatusDTOS;
        }catch(Exception ex){
            throw new RuntimeException("Không tìm thấy thông tin người dùng. Vui lòng đăng nhập lại.");
        }

    }


}
