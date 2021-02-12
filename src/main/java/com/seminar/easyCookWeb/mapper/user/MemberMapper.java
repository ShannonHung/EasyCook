package com.seminar.easyCookWeb.mapper.user;

import com.seminar.easyCookWeb.model.user.EmployeeRequest;
import com.seminar.easyCookWeb.model.user.MemberRequest;
import com.seminar.easyCookWeb.model.user.MemberResponse;
import com.seminar.easyCookWeb.pojo.appUser.Employee;
import com.seminar.easyCookWeb.pojo.appUser.Member;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    MemberMapper MAPPER = Mappers.getMapper( MemberMapper.class );

    MemberResponse toModel(Member member);

    Member toPOJO(MemberResponse request);

    List<MemberResponse> toModels(List<Member> members);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "role",ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(MemberRequest memberRequest, @MappingTarget Member member);

    @Mapping(target = "id",ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateByEmployee(MemberRequest memberRequest, @MappingTarget Member member);

}
