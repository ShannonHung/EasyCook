package com.seminar.easyCookWeb.Controller.User;

import com.seminar.easyCookWeb.Entity.User.EmployeeResponse;
import com.seminar.easyCookWeb.Service.User.MemberService;
import com.seminar.easyCookWeb.Entity.User.MemberRequest;
import com.seminar.easyCookWeb.Entity.User.MemberResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/member" , produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "會員Member", description = "提供會員Member相關的 Rest API")
public class MemberController {
    @Autowired
    MemberService memberService;

    @ApiOperation("會員註冊: Member Register (Role: ROLE_EMPLOYEE)")
    @PostMapping("/register")
    public ResponseEntity<MemberResponse> createMember(@Valid @RequestBody MemberRequest request){
        MemberResponse member = memberService.saveMember(request);
        return new ResponseEntity<MemberResponse>(member, HttpStatus.CREATED);
    }

    @GetMapping(path = "/me")
    @ApiOperation("會員取得自己的資料: Member Get Self Info (Role: ROLE_MEMBER)")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    public ResponseEntity<MemberResponse> findSelf(Authentication authentication){
        MemberResponse response = memberService.getMemberResponseByName(authentication.getName());
        return new ResponseEntity<MemberResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation("透過ID找尋Member: Find Member By Id (Role: ROLE_EMPLOYEE)")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE')")
    public ResponseEntity<MemberResponse> getUser(@PathVariable("id") Long id) {
        //可以設定成 先去ParseToken取得裡面的id是否跟url的id一樣，如果沒有就丟Exception
        MemberResponse member = memberService.getMemberResponseById(id);
        return ResponseEntity.ok(member);
    }

    @GetMapping("/allMembers")
    @ApiOperation("查看所有會員: Find All Members")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE')") //'ROLE_EMPLOYEE'
    public ResponseEntity<List<MemberResponse>> getMembers() {
        List<MemberResponse> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

}
