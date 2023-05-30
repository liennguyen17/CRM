package com.example.democrm.repository;

import com.example.democrm.etity.CustomerGroup;
import com.example.democrm.etity.CustomerStatus;
import com.example.democrm.etity.Customers;
import com.example.democrm.etity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CustomersRepository extends JpaRepository<Customers, Long>, JpaSpecificationExecutor<Customers> {

    List<Customers> findAllByCustomerStatus(CustomerStatus customerStatus);

    long countByCustomerStatus(CustomerStatus customerStatus);

    long countByCustomerStatusAndCreatedDateBetween(CustomerStatus customerStatus, Timestamp startDate, Timestamp endDate);

    List<Customers> findByCustomerGroupAndCustomerStatus(CustomerGroup customerGroup, Long statusId);

    List<Customers> findAllByCustomerGroup_CustomerGroupId(Long customerGroupId);

    //giống với cái trên
    List<Customers> getAllByCustomerGroup_CustomerGroupId(Long customerGroupId);

    List<Customers> findAllByUser_UserId(Long userId);

    List<Customers> findByUserAndCustomerStatus_CustomerStatusId(User user, Long customerStatusId);
}
