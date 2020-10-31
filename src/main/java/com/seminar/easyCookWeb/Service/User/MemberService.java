package com.seminar.easyCookWeb.Service.User;

import com.seminar.easyCookWeb.Converter.MemberConverter;
import com.seminar.easyCookWeb.Exception.ConflictException;
import com.seminar.easyCookWeb.Exception.NotFoundException;
import com.seminar.easyCookWeb.Repository.Users.MemberRepository;
import com.seminar.easyCookWeb.pojo.app_user.Member;
import com.seminar.easyCookWeb.entity.User.MemberRequest;
import com.seminar.easyCookWeb.entity.User.MemberResponse;
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
        log.warn("[Save Member]-> db password is ->" + passwordEncoder.encode(request.getPassword()));
        log.warn("[Save Member]-> db password is ->" + request.toString());

        member.setPassword(passwordEncoder.encode(request.getPassword()));
        member = memberRepository.save(member);
        return MemberConverter.toMemberResponse(member);
    }
    public MemberResponse getMemberResponseById(Long id){
        Member member = Optional.ofNullable(memberRepository.getOne(id))
                .orElseThrow(() -> new NotFoundException("[Find Member By Id] -> {Error} Can't find user."));
        return MemberConverter.toMemberResponse(member);
    }
    public MemberResponse getMemberResponseByName(String username){
        Member member = memberRepository.findByAccount(username)
                .orElseThrow(() -> new NotFoundException("[Find Member By Id] -> {Error} Can't find user."));
        return MemberConverter.toMemberResponse(member);
    }
    public List<MemberResponse> getAllMembers(){
        List<Member> members = memberRepository.findAll();
        return MemberConverter.toMemberResponses(members);
    }
}
