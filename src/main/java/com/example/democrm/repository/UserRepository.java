package com.example.democrm.repository;

import com.example.democrm.etity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByUserName(String userName);
    boolean existsAllByUserName(String userName);
    boolean existsAllByUserNameAndUserIdNot(String userName, Long userId);
    boolean existsAllByEmail(String email);

}
