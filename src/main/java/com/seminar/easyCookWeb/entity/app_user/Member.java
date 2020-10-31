package com.seminar.easyCookWeb.entity.app_user;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Nationalized;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.List;

@Entity
@ToString
@Data
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private long id;

    @Column
    private String account;

    @Column
    private String salt;

    @Column
    private String password;

    @Column(length = 65)
    @Nationalized
    private String username;

    @Column(length = 65)
    private String phoneNum;//如果你這裡使用Num大寫，寫入資料庫會變成phone_num

    @Column
    private String email;


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="employee_week",
            joinColumns = @JoinColumn(name="employee_id"))
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @Column
    @Enumerated(EnumType.STRING)
    private List<UserAuthority> authorities;


    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
