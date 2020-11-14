package com.seminar.easyCookWeb.Service.User;

import com.seminar.easyCookWeb.Converter.MemberConverter;
import com.seminar.easyCookWeb.Exception.ConflictException;
import com.seminar.easyCookWeb.Exception.EntityNotFoundException;
import com.seminar.easyCookWeb.Pojo.appUser.Role;
import com.seminar.easyCookWeb.Repository.Users.MemberRepository;
import com.seminar.easyCookWeb.Pojo.appUser.Member;
import com.seminar.easyCookWeb.Entity.User.MemberRequest;
import com.seminar.easyCookWeb.Entity.User.MemberResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class MemberService {
    MemberRepository memberRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository){
        this.memberRepository= memberRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }


    public MemberResponse saveMember(MemberRequest request){
        Optional<Member> existingMember = memberRepository.findByAccount(request.getAccount());
        if(existingMember.isPresent()){
            throw new ConflictException("[Save Member] -> {Error} This account Name has been used!");
        }
        Member member = MemberConverter.toMember(request);
        member.setRole(Role.MEMBER);
        member.setPassword(passwordEncoder.encode(request.getPassword()));
        member = memberRepository.save(member);
        return MemberConverter.toMemberResponse(member);
    }
    public MemberResponse getMemberResponseById(Long id){
        Member member = memberRepository.findById(id).orElseThrow(() ->new EntityNotFoundException(Member.class, "id", id.toString()));
        return MemberConverter.toMemberResponse(member);
    }
    public MemberResponse getMemberResponseByName(String account){
        Member member = memberRepository.findByAccount(account).orElseThrow(() ->new EntityNotFoundException(Member.class, "account", account));
        return MemberConverter.toMemberResponse(member);
    }
    public List<MemberResponse> getAllMembers(){
        List<Member> members = memberRepository.findAll();
        return MemberConverter.toMemberResponses(members);
    }
}
