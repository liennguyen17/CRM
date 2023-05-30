package com.example.democrm.service;

import com.example.democrm.dto.StatisticsCustomerStatusDTO;
import com.example.democrm.dto.StatisticsStaffDTO;
import com.example.democrm.etity.CustomerGroup;
import com.example.democrm.etity.CustomerStatus;
import com.example.democrm.etity.Customers;
import com.example.democrm.etity.User;
import com.example.democrm.repository.CustomerGroupRepository;
import com.example.democrm.repository.CustomerStatusRepository;
import com.example.democrm.repository.CustomersRepository;
import com.example.democrm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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

//    @Override
//    public List<StatisticsCustomerStatusDTO> statisticsCustomerStatus() {
//        List<Customers> customers = customersRepository.findAll();
//        List<CustomerStatus> statuses = customerStatusRepository.findAll();
//        Map<CustomerStatus, Integer> statusCountMap = new HashMap<>();
//
//        for (CustomerStatus status : statuses){
//            int count = 0;
//            for (Customers customer : customers){
//                if(customer.getCustomerStatus().equals(status)){
//                    count++;
//                }
//            }
//            statusCountMap.put(status, count);
//        }
//
//        int totalCustomers = customers.size();
//
//        List<StatisticsCustomerStatusDTO> customerStatusDTOS = new ArrayList<>();
//
//        for (CustomerStatus status : statuses){
//            int count = statusCountMap.get(status);
//            double percentage = ((double) count/totalCustomers) * 100;
//            StatisticsCustomerStatusDTO customerStatusDTO = new StatisticsCustomerStatusDTO();
//            customerStatusDTO.setType(status.getStatusName());
//            customerStatusDTO.setValue(percentage);
//            customerStatusDTOS.add(customerStatusDTO);
//        }
//        return customerStatusDTOS;
//    }

    //thống kê tất cả trạng thái khách hàng theo tháng hiện tại

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


    @Override
    public List<StatisticsStaffDTO> getStatisticStaffByWeek() {
        List<StatisticsStaffDTO> statisticsStaffDTOArrayList = new ArrayList<>();

        // Lấy ngày hiện tại
        LocalDate currentDate = LocalDate.now();

        // Lấy ngày đầu tiên của tuần hiện tại
        LocalDate firstDayOfWeek = currentDate.with(java.time.DayOfWeek.MONDAY);

        // Lấy thời điểm đầu tiên của ngày đầu tiên của tuần hiện tại
        LocalDateTime startOfWeek = firstDayOfWeek.atStartOfDay();

        // Chuyển thời điểm đầu tiên của ngày đầu tiên của tuần hiện tại sang kiểu dữ liệu Date
        Date startDate = Date.from(startOfWeek.atZone(ZoneId.systemDefault()).toInstant());

        // Lấy thời điểm cuối cùng của ngày cuối cùng của tuần hiện tại
        LocalDateTime endOfWeek = currentDate.with(java.time.DayOfWeek.SUNDAY).atTime(LocalTime.MAX);

        // Chuyển thời điểm cuối cùng của ngày cuối cùng của tuần hiện tại sang kiểu dữ liệu Date
        Date endDate = Date.from(endOfWeek.atZone(ZoneId.systemDefault()).toInstant());

        List<User> staffList = userRepository.findAllByIsSuperAdminFalse();

        for (User staff : staffList) {
            // Lấy danh sách nhóm khách hàng được quản lý bởi nhân viên đang xét
            List<CustomerGroup> customerGroupList = customerGroupRepository.findByUser(staff.getUserId());

            // Khởi tạo biến đếm số lượng khách hàng tiềm năng
            int sales = 0;

            for (CustomerGroup customerGroup : customerGroupList) {
                // Lấy danh sách khách hàng thuộc nhóm khách hàng và có trạng thái là tiềm năng
                List<Customers> customersList = customersRepository.findByCustomerGroupAndCustomerStatus(customerGroup, 1L);

                // Lặp qua danh sách khách hàng để kiểm tra xem khách hàng đó có thuộc tuần hiện tại không
                for (Customers customer : customersList) {
                    if (customer.getCreatedDate().after(startDate) && customer.getCreatedDate().before(endDate)) {
                        sales++;
                    }
                }
            }



            StatisticsStaffDTO statisticsStaffDTO = new StatisticsStaffDTO();
            statisticsStaffDTO.setType(staff.getUserName());
            statisticsStaffDTO.setSale(sales);
            statisticsStaffDTOArrayList.add(statisticsStaffDTO);
        }

        return statisticsStaffDTOArrayList;

    }



    @Override
    public List<StatisticsStaffDTO> getStatisticStaffByWeek1() {
        List<StatisticsStaffDTO> statisticsStaffDTOArrayList = new ArrayList<>();

        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfWeek = currentDate.with(java.time.DayOfWeek.MONDAY);
        LocalDateTime startOfWeek = firstDayOfWeek.atStartOfDay();
//        Date startDate = Date.from(startOfWeek.atZone(ZoneId.systemDefault()).toInstant());
        Timestamp startDate = Timestamp.valueOf(startOfWeek);
        LocalDateTime endOfWeek = currentDate.with(java.time.DayOfWeek.SUNDAY).atTime(LocalTime.MAX);
//        Date endDate = Date.from(endOfWeek.atZone(ZoneId.systemDefault()).toInstant());
        Timestamp endDate = Timestamp.valueOf(endOfWeek);

        List<User> staffList = userRepository.findAllByIsSuperAdminFalse();

        for (User staff : staffList) {
            List<CustomerGroup> customerGroupList = customerGroupRepository.findByUser(staff.getUserId());
            int sales = 0;

            for (CustomerGroup customerGroup : customerGroupList) {
                List<Customers> customersList = customersRepository.findByCustomerGroupAndCustomerStatus(customerGroup, 4L);
                for (Customers customer : customersList) {
//                    if (customer.getCreatedDate().after(startDate) && customer.getCreatedDate().before(endDate)) {
//                        sales++;
//                    }

                    Timestamp customerTimestamp = customer.getCreatedDate();
                    if (customerTimestamp.after(startDate) && customerTimestamp.before(endDate)) {
                        sales++;
                    }

                }
            }

            StatisticsStaffDTO statisticsStaffDTO = new StatisticsStaffDTO();
            statisticsStaffDTO.setType(staff.getUserName());
            statisticsStaffDTO.setSale(sales);
            statisticsStaffDTOArrayList.add(statisticsStaffDTO);
        }

        return statisticsStaffDTOArrayList;

    }
}
