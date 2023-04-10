package com.example.democrm.repository;

import com.example.democrm.etity.Customers;
import com.example.democrm.request.customers.FilterCustomerRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Repository
public class CustomCustomerRepository {
    public static Specification<Customers> filterSpecification(Date dateFrom, Date dateTo,
                                                               FilterCustomerRequest request) {
        return (((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (dateFrom != null && dateTo != null) {
                predicates.add(criteriaBuilder.between(root.get("createdDate"), dateFrom, dateTo));
            }
            if (StringUtils.hasText(request.getCustomerName())) {
                predicates.add(criteriaBuilder.like(root.get("customerName"), "%" + request.getCustomerName() + "%"));
            }
            if (StringUtils.hasText(request.getPhone())) {
                predicates.add(criteriaBuilder.like(root.get("phone"), "%" + request.getPhone() + "%"));
            }
            if (StringUtils.hasText(request.getEmail())) {
                predicates.add(criteriaBuilder.equal(root.get("email"), request.getEmail()));
            }
            if (StringUtils.hasText(request.getNote())) {
                predicates.add(criteriaBuilder.like(root.get("note"), "%" + request.getNote() + "%"));
            }
            if (StringUtils.hasText(request.getAddress())) {
                predicates.add(criteriaBuilder.like(root.get("address"), "%" + request.getAddress() + "%"));
            }
            if (request.getStatusId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("customerStatus"), request.getStatusId()));
            }
            if (request.getCustomerGroupId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("customerGroup"), request.getCustomerGroupId()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }));
    }

}
