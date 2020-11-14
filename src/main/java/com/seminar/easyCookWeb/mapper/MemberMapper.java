package com.seminar.easyCookWeb.mapper;

import com.seminar.easyCookWeb.model.user.MemberResponse;
import com.seminar.easyCookWeb.pojo.appUser.Member;
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
