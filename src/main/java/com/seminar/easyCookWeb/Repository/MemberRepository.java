package com.seminar.easyCookWeb.Repository;

import com.seminar.easyCookWeb.entity.app_user.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByAccount(String account);

}
