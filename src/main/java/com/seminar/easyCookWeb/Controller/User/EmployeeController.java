package com.seminar.easyCookWeb.Controller.User;

import com.seminar.easyCookWeb.Service.User.EmployeeService;
import com.seminar.easyCookWeb.Entity.User.EmployeeRequest;
import com.seminar.easyCookWeb.Entity.User.EmployeeResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
        EmployeeResponse employee = employeeService.saveEmployee(request);
        return new ResponseEntity<EmployeeResponse>(employee, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @ApiOperation("透過ID找尋Employee: Find Employee By Id")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE')")
    public ResponseEntity<EmployeeResponse> getUser(@PathVariable("id") Long id) {
        //可以設定成 先去ParseToken取得裡面的id是否跟url的id一樣，如果沒有就丟Exception
        EmployeeResponse employee = employeeService.getEmployeeResponseById(id);
        return ResponseEntity.ok(employee);
    }

    @GetMapping("/allEmployees")
    @ApiOperation("查看所有員工: Find All Employees")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE')")
    public ResponseEntity<List<EmployeeResponse>> getEmployees() {
        List<EmployeeResponse> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

}