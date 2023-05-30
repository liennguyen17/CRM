package com.example.democrm.repository;

import com.example.democrm.etity.User;
import com.example.democrm.request.user.FilterUserRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class CustomUserRepository {
    public static Specification<User> filterSpecification(Date dateFrom, Date dateTo,
                                                          Date dateOfBirthFrom, Date dateOfBirthTo,
                                                          FilterUserRequest request) {
        return (((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            //always not list superadmin for api
            // luôn không liệt kê Superadmin cho API
            predicates.add(criteriaBuilder.equal(root.get("isSuperAdmin"), false)); //xem co anh huong gi den viec tim kiem theo isSuperAdmin ko
            if (dateFrom != null && dateTo != null) {
                predicates.add(criteriaBuilder.between(root.get("createdDate"), dateFrom, dateTo));
            }
            if (dateOfBirthFrom != null && dateOfBirthTo != null) {
                predicates.add(criteriaBuilder.between(root.get("date"), dateOfBirthFrom, dateOfBirthTo));
            }
            if (StringUtils.hasText(request.getUserName())) {
                predicates.add(criteriaBuilder.like(root.get("userName"), "%" + request.getUserName() + "%"));
            }
            if (StringUtils.hasText(request.getEmail())) {
                predicates.add(criteriaBuilder.equal(root.get("email"), request.getEmail()));
            }
            if (StringUtils.hasText(request.getAddress())) {
                predicates.add(criteriaBuilder.like(root.get("address"), request.getAddress()));
            }
//            if (request.getIsSuperAdmin() != null) {
//                predicates.add(criteriaBuilder.equal(root.get("isSuperAdmin"), request.getIsSuperAdmin()));
//            }
            if (request.getRoleId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("role"), request.getRoleId()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }));
    }
}
