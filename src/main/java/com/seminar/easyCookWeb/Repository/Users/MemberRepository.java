package com.seminar.easyCookWeb.Repository.Users;

import com.seminar.easyCookWeb.pojo.app_user.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByAccount(String account);

}
