package com.seminar.easyCookWeb.pojo.appUser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Collection;

@Entity
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member extends User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Null
    @Column(name="member_id")
    private long id;

    @NotEmpty
    @Column(length = 45, unique = true)
    private String account;

    @NotEmpty
    @Column(length = 1024)
    @JsonIgnore
    private String password;

    @Column(length = 65)
    @Nationalized
    private String username;

    @Column(columnDefinition = "nvarchar(15)")
    private String phone;//如果你這裡使用Num大寫，寫入資料庫會變成phone_num

    @Column(columnDefinition = "nvarchar(512)")
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
