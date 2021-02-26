package com.seminar.easyCookWeb.repository;

import com.seminar.easyCookWeb.pojo.appUser.Department;
import com.seminar.easyCookWeb.pojo.appUser.Employee;
import com.seminar.easyCookWeb.pojo.appUser.Role;
import com.seminar.easyCookWeb.repository.users.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
@Slf4j
public class UserTest {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    public void testUserCreate(){
        Employee employee = Employee.builder()
                .username("test001")
                .account("test001")
                .password(encoder.encode("123"))
                .role(Role.ADMIN)
                .department(Department.Sales)
                .title("員工").build();
        employee = employeeRepository.save(employee);

        employee.setPhone("0978232062");
        employee = employeeRepository.save(employee);

        Assertions.assertEquals("0978232062", employee.getPhone());
    }
}
