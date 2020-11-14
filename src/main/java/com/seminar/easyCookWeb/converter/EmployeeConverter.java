package com.seminar.easyCookWeb.converter;

import com.seminar.easyCookWeb.model.user.EmployeeRequest;
import com.seminar.easyCookWeb.model.user.EmployeeResponse;
import com.seminar.easyCookWeb.pojo.appUser.Employee;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class EmployeeConverter {
    public static Employee toEmployee(EmployeeRequest request){
        Employee employee = new Employee();
        BeanUtils.copyProperties(request, employee);
        System.out.println("[EmployeeConverter] -> EmployeeRequest to Employee -> " + employee);
        return employee;
    }

    public static EmployeeResponse toEmployeeResponse(Employee employee){
        EmployeeResponse response = new EmployeeResponse();
        BeanUtils.copyProperties(employee, response);
        return response;
    }

    public static List<EmployeeResponse> toEmployeeResponses(List<Employee> employees){
        employees.stream().forEach(System.out::println);
        return employees.stream()
                .map(EmployeeConverter::toEmployeeResponse)
                .collect(Collectors.toList());
    }
}
