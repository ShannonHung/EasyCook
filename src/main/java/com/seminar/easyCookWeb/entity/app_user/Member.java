package com.seminar.easyCookWeb.entity.app_user;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.util.List;

@Entity
@ToString
public class Member {
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
    @Column
    @Enumerated(EnumType.STRING)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @CollectionTable(name = "authority", joinColumns = @JoinColumn(name = "member_id"))
    private List<UserAuthority> authorities;


    public Member(){
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<UserAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<UserAuthority> authorities) {
        this.authorities = authorities;
    }
}
