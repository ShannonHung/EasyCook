package com.seminar.easyCookWeb.service.user;

import com.seminar.easyCookWeb.exception.BusinessException;
import com.seminar.easyCookWeb.exception.EntityCreatedConflictException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.user.EmployeeMapper;
import com.seminar.easyCookWeb.model.recipe.RecipeModel;
import com.seminar.easyCookWeb.pojo.appUser.Role;
import com.seminar.easyCookWeb.repository.users.EmployeeRepository;
import com.seminar.easyCookWeb.model.user.EmployeeRequest;
import com.seminar.easyCookWeb.model.user.EmployeeResponse;
import com.seminar.easyCookWeb.pojo.appUser.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
        if(employeeRepository.findByAccount(request.getAccount()).isPresent()){
            throw new EntityCreatedConflictException("[Save Employee] -> {Error} This account Name has been used!");
        }else if(employeeRepository.findByEmail(request.getEmail()).isPresent()){
            throw new EntityCreatedConflictException("[Save Employee] -> {Error} This email has been used!");
        }
        else if(request.getAccount()==null || request.getPassword()==null || request.getAccount()=="" || request.getPassword()==""){
            throw new HttpMessageNotReadableException("Account or Password is Empty");
        }else{
            request.setPassword(passwordEncoder.encode(request.getPassword()));
            Employee employee = mapper.toPOJO(request);
//            employee.setRole(Role.EMPLOYEE);
            employee = employeeRepository.save(employee);
            System.out.println("<Test Mapping> -> "+ Optional.of(mapper.toModel(employee).toString()));
            return Optional.of(mapper.toModel(employee));
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
    public Optional<EmployeeResponse> delete(Long id){
        return employeeRepository.findById(id)
                .map(it ->{
                    try {
                        employeeRepository.deleteById(it.getId());
                        return it;
                    }catch (Exception ex){
                        throw new BusinessException("Cannot Deleted " +it.getId()+ " employee");
                    }
                })
                .map(mapper::toModel);
    }

    public Optional<EmployeeResponse> update(Long iid, EmployeeRequest employeeRequest, Authentication authentication) {
        return Optional.of(employeeRepository.findById(iid))
                .map(it -> {
                    Employee originEmployee = it.orElseThrow(() -> new EntityNotFoundException("Cannot find employee"));

                    log.info("check query => " + employeeRepository.ExistAccountandEmail(employeeRequest.getAccount(), employeeRequest.getEmail(), originEmployee.getId()));
                    log.info("check role  => " + !authentication.getAuthorities().equals(Role.ADMIN));
                    if(authentication.getName() != originEmployee.getAccount() && !authentication.getAuthorities().stream().findFirst().get().toString().equals(Role.ROLE_ADMIN)){
                        throw new BusinessException("You are not the employee you want to update, so you cannot update this employee");
                    }else if(employeeRepository.ExistAccountandEmail(employeeRequest.getAccount(), employeeRequest.getEmail(), originEmployee.getId()) > 0){
                        throw new EntityCreatedConflictException("this account or email have already in used!");
                    }else{
                        if(employeeRequest.getPassword() != null){
                            employeeRequest.setPassword(passwordEncoder.encode(employeeRequest.getPassword()));
                        }
                        mapper.update(employeeRequest, originEmployee);
                        return originEmployee;
                    }
                })
                .map(employeeRepository::save)
                .map(mapper::toModel);
    }
}
