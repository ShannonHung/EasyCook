package com.seminar.easyCookWeb.controller.user;

import com.seminar.easyCookWeb.exception.BusinessException;
import com.seminar.easyCookWeb.exception.EntitiesErrorException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.model.user.UpdatePwd;
import com.seminar.easyCookWeb.pojo.appUser.Employee;
import com.seminar.easyCookWeb.service.user.EmployeeService;
import com.seminar.easyCookWeb.model.user.EmployeeRequest;
import com.seminar.easyCookWeb.model.user.EmployeeResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/employee" , produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "員工Employee連接口", description = "提供員工相關的 Rest API")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @PostMapping("/register")
    @ApiOperation("員工註冊: Employee Register")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody EmployeeRequest request){
//        EmployeeResponse employee = employeeService.saveEmployee(request);
//        return new ResponseEntity<EmployeeResponse>(employee, HttpStatus.CREATED);
        return employeeService.saveEmployee(request)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntityNotFoundException(EmployeeController.class,"Cannot Found Employee"));
    }

    @GetMapping(path = "/me")
    @ApiOperation("員工取得自己的資料: Employee Get Self Info (Role: ROLE_EMPLOYEE)")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<EmployeeResponse> findSelf(Authentication authentication){
//        EmployeeResponse response = employeeService.getEmployeeResponseByName(authentication.getName());
//        return new ResponseEntity<EmployeeResponse>(response, HttpStatus.OK);
        log.info("Employee want to see himself =>" + authentication.getName());
        return employeeService.getEmployeeResponseByAccount(authentication.getName())
                .map(ResponseEntity::ok)
                .orElseThrow(()->new EntityNotFoundException(EmployeeController.class, "name", authentication.getName()));
    }

    @GetMapping("/{id}")
    @ApiOperation("透過ID找尋Employee: Find Employee By Id (Role: ROLE_EMPLOYEE)")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE')")
    public ResponseEntity<EmployeeResponse> getUser(@PathVariable("id") Long id) {
        //可以設定成 先去ParseToken取得裡面的id是否跟url的id一樣，如果沒有就丟Exception
//        EmployeeResponse employee = employeeService.getEmployeeResponseById(id);
//        return ResponseEntity.ok(employee);
        return employeeService.getEmployeeResponseById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(()->new EntityNotFoundException(Employee.class, "id", id.toString()));
    }

    @GetMapping("/allEmployees")
    @ApiOperation("查看所有員工: Find All Employees (Role: 'ROLE_ADMIN')")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<List<EmployeeResponse>> getEmployees() {
//        List<EmployeeResponse> employees = employeeService.getAllEmployees();
//        return ResponseEntity.ok(employees);
        return employeeService.getAllEmployees()
                .map(ResponseEntity::ok)
                .orElseThrow(()->new EntityNotFoundException(Employee.class, "Employee List Not Found"));
    }

    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    @ApiOperation("透過id刪除特定員工: Delete Employees By Id (Role: ROLE_ADMIN)")
    @DeleteMapping(path = "/delete/{employeeId}")
    public ResponseEntity<EmployeeResponse> deleteById(@PathVariable Long employeeId) {
        return employeeService.delete(employeeId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new BusinessException("Delete Employee fail"));
    }

    /**
     * 員工們更新個人資料
     * @param employeeId
     * @param employeeRequest
     * @param authentication
     * @return
     */
    @PatchMapping("/update/data/{employeeId}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    @ApiOperation("透過id來更新員工: Update Employees By Id (Role: ROLE_EMPLOYEE) ****只能員工自己更新自己的****")
    public ResponseEntity<EmployeeResponse> updateByEmployee(@PathVariable Long employeeId, @RequestBody EmployeeRequest employeeRequest, Authentication authentication) {
        return employeeService.update(employeeId, employeeRequest, authentication)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntitiesErrorException("Cannot update employee"));
    }

    @PatchMapping("/update/role/{employeeId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ApiOperation("透過id來更新員工角色: Update Employees By Role (Role: ROLE_ADMIN)  ****admin只能更新員工的腳色****")
    public ResponseEntity<EmployeeResponse> updateByAdmin(@PathVariable Long employeeId, @RequestBody EmployeeRequest employeeRequest) {
        return employeeService.updatebyadmin(employeeId, employeeRequest)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntitiesErrorException("Cannot update employee"));
    }

    @PatchMapping("/update/pwd/{employeeId}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    @ApiOperation("透過id來更新員工密碼: Update Employees' password By Id (Role: ROLE_ADMIN, ROLE_EMPLOYEE)")
    public ResponseEntity<EmployeeResponse> updatePassword(@PathVariable Long employeeId, @Valid @RequestBody UpdatePwd updatePwd, Authentication authentication){
        return employeeService.updatePwd(employeeId, updatePwd, authentication)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntitiesErrorException("Cannot update employee"));
    }


}
