package com.seminar.easyCookWeb.service.user;

import com.seminar.easyCookWeb.converter.EmployeeConverter;
import com.seminar.easyCookWeb.exception.ConflictException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.user.EmployeeMapper;
import com.seminar.easyCookWeb.pojo.appUser.Role;
import com.seminar.easyCookWeb.repository.users.EmployeeRepository;
import com.seminar.easyCookWeb.model.user.EmployeeRequest;
import com.seminar.easyCookWeb.model.user.EmployeeResponse;
import com.seminar.easyCookWeb.pojo.appUser.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
    EmployeeMapper mapper;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper mapper){
        this.employeeRepository= employeeRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.mapper= mapper;
    }


    public Optional<EmployeeResponse> saveEmployee(EmployeeRequest request){
        Optional<Employee> existingEmployee = employeeRepository.findByAccount(request.getAccount());
        if(existingEmployee.isPresent()){
            throw new ConflictException("[Save Employee] -> {Error} This account Name has been used!");
        }else if(request.getAccount()==null || request.getPassword()==null){
            throw new HttpMessageNotReadableException("Account or Password is Empty");
        }else{
            Employee employee = EmployeeConverter.toEmployee(request);
            employee.setRole(Role.EMPLOYEE);
            employee.setPassword(passwordEncoder.encode(request.getPassword()));
            employee = employeeRepository.save(employee);
            System.out.println("<Test Mapping> -> "+Optional.ofNullable(mapper.toModel(employee).toString()));
            return Optional.ofNullable(mapper.toModel(employee));
        }
    }
    public Optional<EmployeeResponse> getEmployeeResponseById(Long id){
        Employee employee = employeeRepository.findById(id).orElseThrow(() ->new EntityNotFoundException(Employee.class, "id", id.toString()));
        return Optional.ofNullable(mapper.toModel(employee));
    }
    public Optional<EmployeeResponse> getEmployeeResponseByAccount(String account){
        Employee employee = employeeRepository.findByAccount(account).orElseThrow(() ->new EntityNotFoundException(Employee.class, "account", account));
        return Optional.ofNullable(mapper.toModel(employee));
    }
    public Optional<List<EmployeeResponse>> getAllEmployees(){
        List<Employee> employees = employeeRepository.findAll();
        return Optional.ofNullable(mapper.toModels(employees));
    }
}
