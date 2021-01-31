package com.seminar.easyCookWeb.pojo.appUser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @Column(name="member_id")
    private long id;

    @NotBlank
    @Column(length = 45, unique = true, nullable = false)
    private String account;

    @NotBlank
    @Column(length = 1024)
    @JsonIgnore
    private String password;

    @NotBlank
    @Column(columnDefinition = "nvarchar(128)")
    @Nationalized
    private String username;

    @Column(columnDefinition = "nvarchar(15)")
    private String phone;//如果你這裡使用Num大寫，寫入資料庫會變成phone_num

    @Column(columnDefinition = "nvarchar(128)")
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
