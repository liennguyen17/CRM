package com.example.democrm.etity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "customer_status")

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerStatusId;

    @Column(name = "status_name", length = 100)
    private String statusName;

    @OneToMany(mappedBy = "customerStatus")
    private Set<Customers> customers;
}
