package com.example.democrm.etity;

import com.example.democrm.constant.RoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.sql.Timestamp;
import java.util.*;

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

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "update_date")
    private Timestamp updateDate;

    @Column(name = "is_super_admin")
    private Boolean isSuperAdmin = false;

    @OneToMany(mappedBy = "user")
    private Set<CustomerGroup> customerGroups;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    //tra ve danh sach doi tuong GrantedAuthority cho nguoi dung dang duoc xac thuc
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        //kt quyen duoc phan
        if (role != null) {
            role.getPermissions().forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getPermissionId())));
        }
        //cho phep quyen admin, neu nguoi dung duoc xac thuc la mot isSuperAdmin ta them quyen ADMIN vao danh sach authorities
        if (isSuperAdmin) {
            authorities.add(new SimpleGrantedAuthority(RoleEnum.ADMIN.name()));
        }
        if (!isSuperAdmin) {
            authorities.add(new SimpleGrantedAuthority(RoleEnum.STAFF.name()));
        }
//        if(isSuperAdmin){
//            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
//        }
        return authorities;
    }


}
