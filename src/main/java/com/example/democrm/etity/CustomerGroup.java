package com.example.democrm.etity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "customer_group")

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerGroupId;

    @OneToMany(mappedBy = "customerGroup")
    private Set<Customers> customers;
    @Column(name = "group_name", length = 100)
    private String groupName;
    @Column(name = "created_date")
    private Timestamp createdDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
