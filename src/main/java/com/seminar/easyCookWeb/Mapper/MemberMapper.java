package com.seminar.easyCookWeb.Mapper;

import com.seminar.easyCookWeb.Entity.User.EmployeeRequest;
import com.seminar.easyCookWeb.Entity.User.EmployeeResponse;
import com.seminar.easyCookWeb.Entity.User.MemberResponse;
import com.seminar.easyCookWeb.Pojo.appUser.Employee;
import com.seminar.easyCookWeb.Pojo.appUser.Member;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    MemberMapper MAPPER = Mappers.getMapper( MemberMapper.class );

    MemberResponse toModel(Member member);

    Member toPOJO(MemberResponse request);

    List<MemberResponse> toModels(List<Member> members);
}
