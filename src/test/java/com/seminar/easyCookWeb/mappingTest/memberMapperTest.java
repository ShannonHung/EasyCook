package com.seminar.easyCookWeb.mappingTest;

import com.seminar.easyCookWeb.Entity.User.EmployeeResponse;
import com.seminar.easyCookWeb.Entity.User.MemberResponse;
import com.seminar.easyCookWeb.Mapper.EmployeeMapper;
import com.seminar.easyCookWeb.Mapper.MemberMapper;
import com.seminar.easyCookWeb.Pojo.appUser.Employee;
import com.seminar.easyCookWeb.Pojo.appUser.Member;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@WebAppConfiguration
public class memberMapperTest {
    @Test
    public void toModel(){
        Member member = Member.builder()
                .id(1L)
                .account("admin")
                .password("123")
                .username("hung").build();
        MemberResponse response = MemberMapper.MAPPER.toModel( member );
        System.out.println(response);
    }

    @Test
    public void testMembers(){
        List<Member> members = generateMembers();
        List<MemberResponse> responses = MemberMapper.MAPPER.toModels(members);
        Optional.ofNullable(responses).stream().forEach(System.out::println);
        assertEquals(5, responses.stream().count());
    }

    public List<Member> generateMembers(){
        List<Member> members = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            Member member = Member.builder()
                    .username(i+" username")
                    .account(i+"account")
                    .phone("123")
                    .password("123").build();

            members.add(member);
        }
        return members;
    }
}
