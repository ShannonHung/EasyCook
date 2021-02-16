package com.seminar.easyCookWeb.service.user;

import com.seminar.easyCookWeb.converter.MemberConverter;
import com.seminar.easyCookWeb.exception.BusinessException;
import com.seminar.easyCookWeb.exception.EntityCreatedConflictException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.user.MemberMapper;
import com.seminar.easyCookWeb.model.user.EmployeeRequest;
import com.seminar.easyCookWeb.model.user.EmployeeResponse;
import com.seminar.easyCookWeb.pojo.appUser.Employee;
import com.seminar.easyCookWeb.pojo.appUser.Role;
import com.seminar.easyCookWeb.repository.users.MemberRepository;
import com.seminar.easyCookWeb.pojo.appUser.Member;
import com.seminar.easyCookWeb.model.user.MemberRequest;
import com.seminar.easyCookWeb.model.user.MemberResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.Authentication;
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
        log.info("[recieve member register request] => " + request);
        if (request.getAccount()==null || request.getPassword()==null || request.getAccount()=="" || request.getPassword()==""){
            throw new HttpMessageNotReadableException("Account or Password is Empty");
        }else{

            Optional<Member> existingMember = memberRepository.findByAccount(request.getAccount());
            if(existingMember.isPresent()){
                throw new EntityCreatedConflictException(("[Save Member] -> {Error} This account Name has been used!"));
            }
            Member member = MemberConverter.toMember(request);
            member.setRole(Role.MEMBER);
            member.setPassword(passwordEncoder.encode(request.getPassword()));
            member = memberRepository.save(member);
            return Optional.ofNullable(mapper.toModel(member));
        }
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
    public Optional<MemberResponse> delete(Long id){
        return memberRepository.findById(id)
                .map(it ->{
                    try {
                        memberRepository.deleteById(it.getId());
                        return it;
                    }catch (Exception ex){
                        throw new BusinessException("Cannot Deleted " +it.getId()+ " member");
                    }
                })
                .map(mapper::toModel);
    }

    public Optional<MemberResponse> update(Long id, MemberRequest memberRequest, Authentication authentication) {
        return Optional.of(memberRepository.findById(id))
                .map(it -> {
                    log.info("auth.getName => " + authentication.getName() + "auth.getDetails=>" + authentication.getDetails());
                    if(!it.get().getAccount().equals(authentication.getName())) throw new BusinessException("You are not the member you want to update, so you cannot update this member");
                    Member originMember = it.orElseThrow(() -> new EntityNotFoundException("Cannot find member"));
                    mapper.update(memberRequest, originMember);
                    return originMember;
                })
                .map(memberRepository::save)
                .map(mapper::toModel);
    }

    public Optional<MemberResponse> updateByEmployee(Long id, MemberRequest memberRequest) {
        return Optional.of(memberRepository.findById(id))
                .map(it -> {
                    Member originMember = it.orElseThrow(() -> new EntityNotFoundException("Cannot find member"));
                    mapper.updateByEmployee(memberRequest, originMember);
                    return originMember;
                })
                .map(memberRepository::save)
                .map(mapper::toModel);
    }
}
