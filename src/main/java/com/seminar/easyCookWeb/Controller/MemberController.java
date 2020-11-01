package com.seminar.easyCookWeb.Controller;

import com.seminar.easyCookWeb.Service.User.MemberService;
import com.seminar.easyCookWeb.Entity.User.MemberRequest;
import com.seminar.easyCookWeb.Entity.User.MemberResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/member" , produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberController {
    @Autowired
    MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<MemberResponse> createMember(@Valid @RequestBody MemberRequest request){
        MemberResponse member = memberService.saveMember(request);
        return new ResponseEntity<MemberResponse>(member, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ROOT', 'ROLE_EMPLOYEE')")
    public ResponseEntity<MemberResponse> getUser(@PathVariable("id") Long id) {
        //可以設定成 先去ParseToken取得裡面的id是否跟url的id一樣，如果沒有就丟Exception
        MemberResponse member = memberService.getMemberResponseById(id);
        return ResponseEntity.ok(member);
    }

    @GetMapping("/allMembers")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ROOT', 'ROLE_EMPLOYEE')")
    public ResponseEntity<List<MemberResponse>> getMembers() {
        List<MemberResponse> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

}
