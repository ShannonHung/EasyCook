package com.seminar.easyCookWeb.Controller;

import com.seminar.easyCookWeb.Service.User.EmployeeService;
import com.seminar.easyCookWeb.Service.User.MemberService;
import com.seminar.easyCookWeb.entity.User.EmployeeRequest;
import com.seminar.easyCookWeb.entity.User.EmployeeResponse;
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
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @PostMapping("/register")
    public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody EmployeeRequest request){
        EmployeeResponse employee = employeeService.saveEmployee(request);
        return new ResponseEntity<EmployeeResponse>(employee, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ROOT', 'ROLE_EMPLOYEE')")
    public ResponseEntity<EmployeeResponse> getUser(@PathVariable("id") Long id) {
        //可以設定成 先去ParseToken取得裡面的id是否跟url的id一樣，如果沒有就丟Exception
        EmployeeResponse employee = employeeService.getEmployeeResponseById(id);
        return ResponseEntity.ok(employee);
    }

    @GetMapping("/allEmployees")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ROOT', 'ROLE_EMPLOYEE')")
    public ResponseEntity<List<EmployeeResponse>> getEmployees() {
        List<EmployeeResponse> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

}
