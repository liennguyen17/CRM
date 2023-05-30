package com.example.democrm.repository;

import com.example.democrm.etity.Role;
import com.example.democrm.request.role.FilterRoleRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class CustomRoleRepository {
    public static Specification<Role> filterSpecification(Date dateFrom, Date dateTo,
                                                          FilterRoleRequest request) {
        return (((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (dateFrom != null && dateTo != null) {
                predicates.add(criteriaBuilder.between(root.get("createdDate"), dateFrom, dateTo));
            }
            if (StringUtils.hasText(request.getRoleName())) {
                predicates.add(criteriaBuilder.like(root.get("roleName"), "%" + request.getRoleName() + "%"));
            }
            if (StringUtils.hasText(request.getDescriptionRole())) {
                predicates.add(criteriaBuilder.like(root.get("descriptionRole"), "%" + request.getDescriptionRole() + "%"));
            }
            if (request.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), request.getStatus()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }));
    }
}
