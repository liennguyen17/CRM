package com.example.democrm.etity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "customers")

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    @Column(name = "customer_name", length = 100)
    private String customerName;
    @ManyToOne
    @JoinColumn(name = "customer_status_id")
    private CustomerStatus customerStatus;
    @ManyToOne
    @JoinColumn(name = "customer_group_id")
    private CustomerGroup customerGroup;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "created_date")
    private Timestamp createdDate;
    @Column(name = "update_date")
    private Timestamp updateDate;
    @Column(name = "phone", length = 10)
    private String phone;
    @Column(name = "email", length = 100)
    private String email;
    @Column(name = "note")
    private String note;
    @Column(name = "address")
    private String address;


}
