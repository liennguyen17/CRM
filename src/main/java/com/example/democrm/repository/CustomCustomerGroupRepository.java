package com.example.democrm.repository;

import com.example.democrm.etity.CustomerGroup;
import com.example.democrm.request.customergroup.FilterCustomerGroupRequest;
import jakarta.persistence.criteria.Predicate;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class CustomCustomerGroupRepository {
    public static Specification<CustomerGroup> filterSpecification(Date dateFrom, Date dateTo,
                                                                   FilterCustomerGroupRequest request) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (dateFrom != null && dateTo != null) {
                predicates.add(criteriaBuilder.between(root.get("createdDate"), dateFrom, dateTo));
            }
            if (StringUtils.hasText(request.getGroupName())) {
                predicates.add(criteriaBuilder.like(root.get("groupName"), "%" + request.getGroupName() + "%"));
            }
            if (request.getUserId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("user"), request.getUserId()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });

    }
}
