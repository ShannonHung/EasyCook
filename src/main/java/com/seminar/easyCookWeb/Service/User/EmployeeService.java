package com.seminar.easyCookWeb.Service.User;

import com.seminar.easyCookWeb.Converter.EmployeeConverter;
import com.seminar.easyCookWeb.Exception.ConflictException;
import com.seminar.easyCookWeb.Exception.EntityNotFoundException;
import com.seminar.easyCookWeb.Pojo.app_user.Role;
import com.seminar.easyCookWeb.Repository.Users.EmployeeRepository;
import com.seminar.easyCookWeb.Entity.User.EmployeeRequest;
import com.seminar.easyCookWeb.Entity.User.EmployeeResponse;
import com.seminar.easyCookWeb.Pojo.app_user.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class EmployeeService {
    EmployeeRepository employeeRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository){
        this.employeeRepository= employeeRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }


    public EmployeeResponse saveEmployee(EmployeeRequest request){
        Optional<Employee> existingEmployee = employeeRepository.findByAccount(request.getAccount());
        if(existingEmployee.isPresent()){
            throw new ConflictException("[Save Employee] -> {Error} This account Name has been used!");
        }
        Employee employee = EmployeeConverter.toEmployee(request);
        employee.setRole(Role.EMPLOYEE);
        employee.setPassword(passwordEncoder.encode(request.getPassword()));
        employee = employeeRepository.save(employee);
        return EmployeeConverter.toEmployeeResponse(employee);
    }
    public EmployeeResponse getEmployeeResponseById(Long id){
        Employee employee = employeeRepository.findById(id).orElseThrow(() ->new EntityNotFoundException(Employee.class, "id", id.toString()));
        return EmployeeConverter.toEmployeeResponse(employee);
    }
    public EmployeeResponse getEmployeeResponseByName(String account){
        Employee employee = employeeRepository.findByAccount(account).orElseThrow(() ->new EntityNotFoundException(Employee.class, "account", account));
        return EmployeeConverter.toEmployeeResponse(employee);
    }
    public List<EmployeeResponse> getAllEmployees(){
        List<Employee> employees = employeeRepository.findAll();
        return EmployeeConverter.toEmployeeResponses(employees);
    }
}
