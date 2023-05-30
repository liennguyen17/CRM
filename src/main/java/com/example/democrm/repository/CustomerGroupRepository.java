package com.example.democrm.repository;

import com.example.democrm.etity.CustomerGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CustomerGroupRepository extends JpaRepository<CustomerGroup, Long>, JpaSpecificationExecutor<CustomerGroup> {
    List<CustomerGroup> findByUser(Long id);

    List<CustomerGroup> getAllByUser_UserId(Long id);

    List<Long> getAllByCustomerGroupId(Long id);
    List<CustomerGroup> findByCustomerGroupIdIn(List<Long> customerGroupIds);
}
