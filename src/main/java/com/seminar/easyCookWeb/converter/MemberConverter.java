package com.seminar.easyCookWeb.converter;

import com.seminar.easyCookWeb.pojo.appUser.Member;
import com.seminar.easyCookWeb.model.user.MemberRequest;
import com.seminar.easyCookWeb.model.user.MemberResponse;
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
