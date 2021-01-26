package com.seminar.easyCookWeb.controller.user;

import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.service.user.MemberService;
import com.seminar.easyCookWeb.model.user.MemberRequest;
import com.seminar.easyCookWeb.model.user.MemberResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.aspectj.weaver.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/member" , produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "會員Member", description = "提供會員Member相關的 Rest API")
public class MemberController {
    @Autowired
    MemberService memberService;

    @ApiOperation("會員註冊: Member Register (Role: ROLE_EMPLOYEE)")
    @PostMapping("/register")
    public ResponseEntity<MemberResponse> createMember(@Valid @RequestBody MemberRequest request){
        return memberService.saveMember(request)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntityNotFoundException("Cannot Found Member !"));
    }

    @GetMapping(path = "/me")
    @ApiOperation("會員取得自己的資料: Member Get Self Info (Role: ROLE_MEMBER)")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE')")
    public ResponseEntity<MemberResponse> findSelf(Authentication authentication){
        return memberService.getMemberResponseByName(authentication.getName())
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntityNotFoundException(Member.class, "name", authentication.getName()));
    }

    @GetMapping("/{id}")
    @ApiOperation("透過ID找尋Member: Find Member By Id (Role: ROLE_EMPLOYEE)")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE')")
    public ResponseEntity<MemberResponse> getUser(@PathVariable("id") Long id) {
        //可以設定成 先去ParseToken取得裡面的id是否跟url的id一樣，如果沒有就丟Exception
        return memberService.getMemberResponseById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntityNotFoundException(Member.class, "id", id.toString()));
    }

    @GetMapping("/allMembers")
    @ApiOperation("查看所有會員: Find All Members")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE')") //'ROLE_EMPLOYEE'
    public ResponseEntity<List<MemberResponse>> getMembers() {
        return memberService.getAllMembers()
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntityNotFoundException("Cannot found Enitty"));
    }

}
