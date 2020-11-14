package com.seminar.easyCookWeb.repository.users;

import com.seminar.easyCookWeb.pojo.appUser.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByAccount(String account);
    Optional<Member> findById(String id);

}
