package com.seminar.easyCookWeb.Config;

import com.seminar.easyCookWeb.Exception.NotFoundException;
import com.seminar.easyCookWeb.Repository.MemberRepository;
import com.seminar.easyCookWeb.entity.app_user.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
/**Security機制在進行身分驗證的時候，會使用UserDetails interface的物件作為使用者詳情的載體
 該User類別是由Security的函式庫所提供，本身已經實作UserDetails，回傳該物件是較簡單的做法。
 至於建構子的第三個參數是「authorities」，是用來定義使用者擁有的權限。但本節不會運用它，所以給予空List。
 **/
@Slf4j
@Service
public class SpringUserService implements UserDetailsService {
    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        return memberRepository.findByAccount(account)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }
}
