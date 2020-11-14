package com.seminar.easyCookWeb.mappingTest;

import com.seminar.easyCookWeb.Entity.User.EmployeeResponse;
import com.seminar.easyCookWeb.Mapper.EmployeeMapper;
import com.seminar.easyCookWeb.Pojo.appUser.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@WebAppConfiguration
public class MappingTest {

    @Test
    public void hello(){
        Employee employee = Employee.builder()
                .id(1L)
                .account("admin")
                .password("123")
                .username("hung").build();
        EmployeeResponse response = EmployeeMapper.MAPPER.toModel( employee );
        System.out.println(response);
    }

    @Test
    public void testEmployees(){
        List<Employee> employees = generateEmployees();
        List<EmployeeResponse> responses = EmployeeMapper.MAPPER.toModels(employees);
        Optional.ofNullable(responses).stream().forEach(System.out::println);
        assertEquals(5, responses.stream().count());
    }

    public List<Employee> generateEmployees(){
        List<Employee> employees = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            Employee employee = Employee.builder()
                    .username(i+" username")
                    .account(i+"account")
                    .title(i+"title")
                    .phone("123")
                    .password("123").build();

            employees.add(employee);
        }
        return employees;
    }
}
