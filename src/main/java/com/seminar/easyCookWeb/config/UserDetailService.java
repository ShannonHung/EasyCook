package com.seminar.easyCookWeb.config;

import com.seminar.easyCookWeb.repository.users.EmployeeRepository;
import com.seminar.easyCookWeb.repository.users.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**Security機制在進行身分驗證的時候，會使用UserDetails interface的物件作為使用者詳情的載體
 該User類別是由Security的函式庫所提供，本身已經實作UserDetails，回傳該物件是較簡單的做法。
 至於建構子的第三個參數是「authorities」，是用來定義使用者擁有的權限。但本節不會運用它，所以給予空List。
 **/
@Slf4j
@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        if(employeeRepository.findByAccount(account).isPresent()){
            return employeeRepository.findByAccount(account).get();
        }else if(memberRepository.findByAccount(account).isPresent()){
            return memberRepository.findByAccount(account).get();
        }else{
            throw new UsernameNotFoundException("user not found");
        }
    }
}
