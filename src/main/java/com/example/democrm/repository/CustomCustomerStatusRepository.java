package com.example.democrm.repository;

import com.example.democrm.etity.CustomerStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


@Repository
public class CustomCustomerStatusRepository {
    public static Specification<CustomerStatus> buildFilterSpecification(String statusName) {
        return (((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(statusName)) {
                predicates.add(criteriaBuilder.like(root.get("statusName"), "%" + statusName + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }));
    }
}
