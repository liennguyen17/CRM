package com.example.democrm.etity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Table(name = "permission")

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Permission {
    @Id
    private String permissionId;

    @Column(name = "permission_name", length = 100)
    private String permissionName;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private boolean status;

    @ManyToMany(mappedBy = "permissions")
    private Collection<Role> roles;
}
