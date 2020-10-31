package com.seminar.easyCookWeb.Converter;

import com.seminar.easyCookWeb.pojo.app_user.Member;
import com.seminar.easyCookWeb.entity.User.MemberRequest;
import com.seminar.easyCookWeb.entity.User.MemberResponse;

import java.util.List;
import java.util.stream.Collectors;

public class MemberConverter {
    public MemberConverter(){}
    public static Member toMember(MemberRequest request){
        Member member = new Member();
        member.setAccount(request.getAccount());
        member.setPassword(request.getPassword());
        member.setEmail(request.getEmail());
        member.setPhone(request.getPhone());
        member.setUsername(request.getUsername());
        member.setRole(request.getRole());

        return member;
    }

    public static MemberResponse toMemberResponse(Member member){
        MemberResponse response = new MemberResponse();
        response.setId(member.getId());
        response.setAccount(member.getAccount());
        response.setEmail(member.getEmail());
        response.setPhone(member.getPhone());
        response.setRole(member.getRole());
        response.setUsername(member.getUsername());
        return response;
    }

    public static List<MemberResponse> toMemberResponses(List<Member> members){
        members.stream().forEach(System.out::println);

        return members.stream()
                .map(MemberConverter::toMemberResponse)
                .collect(Collectors.toList());
    }
}
