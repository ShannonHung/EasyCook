package com.seminar.easyCookWeb.controller.user;

import com.seminar.easyCookWeb.exception.BusinessException;
import com.seminar.easyCookWeb.exception.EntitiesErrorException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.model.user.*;
import com.seminar.easyCookWeb.service.user.MemberService;
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
@RequestMapping(value = "/member" , produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "會員Member", description = "提供會員Member相關的 Rest API")
public class MemberController {
    @Autowired
    MemberService memberService;

    @ApiOperation("會員註冊: Member Register (Role: ALL)")
    @PostMapping("/register")
    public ResponseEntity<MemberResponse> createMember(@Valid @RequestBody MemberRequest request){
        return memberService.saveMember(request)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntityNotFoundException("Cannot Found Member !"));
    }

    @GetMapping(path = "/me")
    @ApiOperation("會員取得自己的資料: Member Get Self Info (Role: ROLE_MEMBER)")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    public ResponseEntity<MemberResponse> findSelf(Authentication authentication){
        return memberService.getMemberResponseByName(authentication.getName())
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntityNotFoundException(Member.class, "name", authentication.getName()));
    }

    @GetMapping("/{id}")
    @ApiOperation("透過ID找尋Member: Find Member By Id (Role: ROLE_EMPLOYEE)")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<MemberResponse> getUser(@PathVariable("id") Long id) {
        //可以設定成 先去ParseToken取得裡面的id是否跟url的id一樣，如果沒有就丟Exception
        return memberService.getMemberResponseById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntityNotFoundException(Member.class, "id", id.toString()));
    }

    @GetMapping("/allMembers")
    @ApiOperation("查看所有會員: Find All Members (Role: 'ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')") //'ROLE_EMPLOYEE'
    public ResponseEntity<List<MemberResponse>> getMembers() {
        return memberService.getAllMembers()
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntityNotFoundException("Cannot found Enitty"));
    }

    @ApiOperation("透過id刪除特定會員: Delete Member By Id (Role: 'ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')") //'ROLE_EMPLOYEE'
            @DeleteMapping(path = "/delete/{memberId}")
    public ResponseEntity<MemberResponse> deleteById(@PathVariable Long memberId) {
        return memberService.delete(memberId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new BusinessException("Delete member fail"));

    }

    @PatchMapping("/update/data/{memberId}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN', 'ROLE_MEMBER')")
    @ApiOperation("透過id來更新員工(個人資料): Update Employees By Id (Role: ROLE_ADMIN, ROLE_EMPLOYEE, ROLE_MEMBER)")
    public ResponseEntity<MemberResponse> update(@PathVariable Long memberId, @RequestBody MemberRequest memberRequest, Authentication authentication) {
        return memberService.update(memberId, memberRequest, authentication)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntitiesErrorException("Cannot update member"));

    }

    @PatchMapping("/update/role/{memberId}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    @ApiOperation("透過id來更新員工(Role): Update Employees By Id (Role: ROLE_ADMIN, ROLE_EMPLOYEE)")
    public ResponseEntity<MemberResponse> updateByEmployee(@PathVariable Long memberId, @RequestBody MemberRequest memberRequest) {
        return memberService.updateByEmployee(memberId, memberRequest)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntitiesErrorException("Cannot update member"));
    }

    @PatchMapping("/update/pwd/{memberId}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN', 'ROLE_MEMBER')")
    @ApiOperation("透過id來更新會員密碼: Update Employees' password By Id (Role: 'ROLE_EMPLOYEE', 'ROLE_ADMIN', 'ROLE_MEMBER')")
    public ResponseEntity<MemberResponse> updatePassword(@Valid @RequestBody UpdatePwd updatePwd, Authentication authentication){
        return memberService.updatePwd(updatePwd, authentication)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntitiesErrorException("Cannot update employee"));
    }

}
