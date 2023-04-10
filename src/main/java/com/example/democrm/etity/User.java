package com.example.democrm.etity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "user")

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "user_name", length = 100)
    private String userName;

    @Column(name = "date")
    private Date date;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "address")
    private String address;

    @Column(name = "is_super_admin")
    private Boolean isSuperAdmin = false;

    @OneToMany(mappedBy = "user")
    private Set<CustomerGroup> customerGroups;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;


}
