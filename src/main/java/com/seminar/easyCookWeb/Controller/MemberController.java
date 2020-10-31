package com.seminar.easyCookWeb.Controller;

import com.seminar.easyCookWeb.Service.User.MemberService;
import com.seminar.easyCookWeb.entity.app_user.Member;
import com.seminar.easyCookWeb.entity.app_user.MemberRequest;
import com.seminar.easyCookWeb.entity.app_user.MemberResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/member" , produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberController {
    @Autowired
    MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberResponse> createMember(@Valid @RequestBody MemberRequest request){
        MemberResponse member = memberService.saveMember(request);
        return new ResponseEntity<MemberResponse>(member, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ROOT')")
    public ResponseEntity<MemberResponse> getUser(@PathVariable("id") Long id) {
        MemberResponse member = memberService.getMemberResponseById(id);
        return ResponseEntity.ok(member);
    }

    @GetMapping("/allMembers")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ROOT')")
    public ResponseEntity<List<MemberResponse>> getMembers() {
        List<MemberResponse> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

}
