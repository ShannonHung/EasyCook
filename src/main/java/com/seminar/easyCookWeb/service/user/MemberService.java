package com.seminar.easyCookWeb.service.user;

import com.seminar.easyCookWeb.converter.MemberConverter;
import com.seminar.easyCookWeb.exception.ConflictException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.MemberMapper;
import com.seminar.easyCookWeb.pojo.appUser.Role;
import com.seminar.easyCookWeb.repository.users.MemberRepository;
import com.seminar.easyCookWeb.pojo.appUser.Member;
import com.seminar.easyCookWeb.model.user.MemberRequest;
import com.seminar.easyCookWeb.model.user.MemberResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MemberMapper mapper;

    @Autowired
    public MemberService(MemberRepository memberRepository, MemberMapper mapper){
        this.memberRepository= memberRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.mapper = mapper;
    }


    public Optional<MemberResponse> saveMember(MemberRequest request){
        Optional<Member> existingMember = memberRepository.findByAccount(request.getAccount());
        if(existingMember.isPresent()){
            throw new ConflictException("[Save Member] -> {Error} This account Name has been used!");
        }
        Member member = MemberConverter.toMember(request);
        member.setRole(Role.MEMBER);
        member.setPassword(passwordEncoder.encode(request.getPassword()));
        member = memberRepository.save(member);
        return Optional.ofNullable(mapper.toModel(member));
    }
    public Optional<MemberResponse> getMemberResponseById(Long id){
        Member member = memberRepository.findById(id).orElseThrow(() ->new EntityNotFoundException(Member.class, "id", id.toString()));
        return Optional.ofNullable(mapper.toModel(member));
    }
    public Optional<MemberResponse> getMemberResponseByName(String account){
        Member member = memberRepository.findByAccount(account).orElseThrow(() ->new EntityNotFoundException(Member.class, "account", account));
        return Optional.ofNullable(mapper.toModel(member));
    }
    public Optional<List<MemberResponse>> getAllMembers(){
        List<Member> members = memberRepository.findAll();
        return Optional.ofNullable(mapper.toModels(members));
    }
}
