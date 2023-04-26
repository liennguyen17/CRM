package com.example.democrm.repository;

import com.example.democrm.etity.CustomerStatus;
import com.example.democrm.etity.Customers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomersRepository extends JpaRepository<Customers, Long>, JpaSpecificationExecutor<Customers> {

    List<Customers> findAllByCustomerStatus(CustomerStatus customerStatus);

    long countByCustomerStatus(CustomerStatus customerStatus);
}
