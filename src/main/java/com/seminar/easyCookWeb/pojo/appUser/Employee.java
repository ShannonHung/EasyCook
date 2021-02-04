package com.seminar.easyCookWeb.pojo.appUser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Employee extends User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="employee_id")
    private long id;

    @Column(length = 45, unique = true)
    @NotBlank
    private String account;

    @NotNull
    @Column(length = 1024)
    @JsonIgnore
    @NotBlank
    private String password;

    @Column(length = 65)
    @Nationalized
    @NotBlank
    private String username;

    @Column(name = "department", nullable = false, columnDefinition = "nvarchar(30)")
    @Enumerated(EnumType.STRING)
    private Department department;

    @Column(columnDefinition = "nvarchar(30)", nullable = false)
    private String title;

    @Column(columnDefinition = "nvarchar(15)")
    private String phone;//如果你這裡使用phoneNum大寫，寫入資料庫會變成phone_num

    @Column(columnDefinition = "nvarchar(254)")
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Role role;

    @Transient
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(getRole().getName());
    }

    @Transient
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Transient
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Transient
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Transient
    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
