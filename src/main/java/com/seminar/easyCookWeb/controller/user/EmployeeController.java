package com.seminar.easyCookWeb.controller.user;

import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.pojo.appUser.Employee;
import com.seminar.easyCookWeb.service.user.EmployeeService;
import com.seminar.easyCookWeb.model.user.EmployeeRequest;
import com.seminar.easyCookWeb.model.user.EmployeeResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/employee" , produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "員工Employee連接口", description = "提供員工相關的 Rest API")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @PostMapping("/register")
    @ApiOperation("員工註冊: Employee Register")
    public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody EmployeeRequest request){
//        EmployeeResponse employee = employeeService.saveEmployee(request);
//        return new ResponseEntity<EmployeeResponse>(employee, HttpStatus.CREATED);
        return employeeService.saveEmployee(request)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntityNotFoundException(EmployeeController.class,"Cannot Found Employee"));
    }

    @GetMapping(path = "/me")
    @ApiOperation("員工取得自己的資料: Employee Get Self Info (Role: ROLE_EMPLOYEE)")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE')")
    public ResponseEntity<EmployeeResponse> findSelf(Authentication authentication){
//        EmployeeResponse response = employeeService.getEmployeeResponseByName(authentication.getName());
//        return new ResponseEntity<EmployeeResponse>(response, HttpStatus.OK);
        return employeeService.getEmployeeResponseByName(authentication.getName())
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
    @ApiOperation("查看所有員工: Find All Employees (Role: ROLE_EMPLOYEE)")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE')")
    public ResponseEntity<List<EmployeeResponse>> getEmployees() {
//        List<EmployeeResponse> employees = employeeService.getAllEmployees();
//        return ResponseEntity.ok(employees);
        return employeeService.getAllEmployees()
                .map(ResponseEntity::ok)
                .orElseThrow(()->new EntityNotFoundException(Employee.class, "Employee List Not Found"));
    }

}
