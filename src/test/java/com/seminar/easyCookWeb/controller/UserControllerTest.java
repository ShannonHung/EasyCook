package com.seminar.easyCookWeb.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.seminar.easyCookWeb.model.error.ErrorResponse;
import com.seminar.easyCookWeb.model.user.*;
import com.seminar.easyCookWeb.pojo.appUser.Role;
import com.seminar.easyCookWeb.repository.users.MemberRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private JacksonTester<TokenResponse> jsonToken;

    private JacksonTester<MemberRequest> jsonMemberReq;

    private JacksonTester<AuthRequest> authRequest;

    private JacksonTester<MemberResponse> jsonMemberResponse;

    private JacksonTester<Iterable<MemberResponse>> jsonMembers;

    private JacksonTester<ErrorResponse> jsonErrorResponse;


    @BeforeEach
    void init(@Mock MemberRepository memberRepository){
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    public void memberLogin() throws Exception {
       HttpHeaders httpHeaders = new HttpHeaders();
       httpHeaders.add("Content-Type", "application/json");

        JSONObject request = new JSONObject();
        request.put("account", "member1");
        request.put("password", "123");

        mvc.perform(
                post("/login")
                .headers(httpHeaders)
                .content(request.toString())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").hasJsonPath());
    }

    @Test
    public void adminLogin() throws Exception {
        AuthRequest adminEmployee = AuthRequest.builder()
                .account("admin")
                .password("a19519b15b5ae2ddb93f7c67881ab3ea14a38cb9c5912f4f4362840b97894bf3").build();

        MockHttpServletResponse response = mvc.perform(
                post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(authRequest.write(adminEmployee).getJson())
        ).andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();

        String adminToken = jsonToken.parseObject(response.getContentAsString()).getToken();
        assertThat(adminToken).isNotNull();
    }

    @Test
    public void getMeInformation() throws Exception {
        String token = getJsonToken("member1", "123");

        MockHttpServletResponse response = mvc.perform(
                get("/member/me")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();

        MemberResponse memberResponse = jsonMemberResponse.parseObject(response.getContentAsString());
        assertThat(memberResponse.getRole()).isEqualTo(Role.MEMBER);
        assertThat(memberResponse.getAccount()).isEqualTo("member1");
    }

    @Test
    public void deleteMember() throws Exception{
        MemberResponse preMember = CreateNewUser(UUID.randomUUID().toString());
        String employeeToken = getAdminToken();

        MockHttpServletResponse response = mvc.perform(
                delete("/member/delete/"+preMember.getId())
                        .header("Authorization", employeeToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();

    }

//    @Test
//    public void updateMember() throws Exception{
//        MemberResponse preMember = CreateNewUser(UUID.randomUUID().toString());
//        JSONObject request = new JSONObject();
//        request.put("email", "baka@gmail.com");
//        request.put("username", "baka");
//        request.put("account", "baka");
//
//        String token = getJsonToken(preMember.getAccount(), "123");
//
//        MockHttpServletResponse response = mvc.perform(
//                patch("/member/update/data/" + preMember.getId())
//                        .header("Authorization", token)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                .content(request.toString())
//        ).andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.username").value("baka"))
//                .andReturn().getResponse();
//    }

    public MemberResponse CreateNewUser(String username) throws Exception{
        MemberRequest newMember = MemberRequest.builder()
                .account(username)
                .email("newEmail")
                .password("123")
                .phone("123")
                .username("newUser").build();

        MockHttpServletResponse response = mvc.perform(
                post("/member/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonMemberReq.write(newMember).getJson())
        ).andReturn().getResponse();

        return jsonMemberResponse.parseObject(response.getContentAsString());
    }


    public String getAdminToken() throws Exception{
        AuthRequest adminEmployee = AuthRequest.builder()
                .account("admin")
                .password("a19519b15b5ae2ddb93f7c67881ab3ea14a38cb9c5912f4f4362840b97894bf3").build();

        MockHttpServletResponse response = mvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(authRequest.write(adminEmployee).getJson())
        ).andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();

        return jsonToken.parseObject(response.getContentAsString()).getToken();
    }

    public String getJsonToken(String account, String password) throws Exception {
        AuthRequest adminEmployee = AuthRequest.builder()
                .account(account)
                .password(password)
                .build();

        MockHttpServletResponse response = mvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(authRequest.write(adminEmployee).getJson())
        ).andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();

        return jsonToken.parseObject(response.getContentAsString()).getToken();
    }

    public AuthRequest createUser(String account, String password){
        return AuthRequest.builder()
                .account(account)
                .password(password).build();
    }


}
