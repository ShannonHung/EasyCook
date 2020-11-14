package com.seminar.easyCookWeb.Converter;

import com.seminar.easyCookWeb.Pojo.appUser.Member;
import com.seminar.easyCookWeb.Entity.User.MemberRequest;
import com.seminar.easyCookWeb.Entity.User.MemberResponse;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class MemberConverter {
    public MemberConverter(){}
    public static Member toMember(MemberRequest request){
        Member member = new Member();
        BeanUtils.copyProperties(request, member);
        return member;
    }

    public static MemberResponse toMemberResponse(Member member){
        MemberResponse response = new MemberResponse();
        BeanUtils.copyProperties(member, response);
        return response;
    }

    public static List<MemberResponse> toMemberResponses(List<Member> members){
        members.stream().forEach(System.out::println);
        return members.stream()
                .map(MemberConverter::toMemberResponse)
                .collect(Collectors.toList());
    }
}
