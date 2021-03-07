package com.seminar.easyCookWeb.service.user;

import com.seminar.easyCookWeb.exception.BusinessException;
import com.seminar.easyCookWeb.exception.EntityCreatedConflictException;
import com.seminar.easyCookWeb.exception.EntityNotFoundException;
import com.seminar.easyCookWeb.mapper.user.EmployeeMapper;
import com.seminar.easyCookWeb.model.user.UpdatePwd;
import com.seminar.easyCookWeb.pojo.appUser.Role;
import com.seminar.easyCookWeb.repository.users.EmployeeRepository;
import com.seminar.easyCookWeb.model.user.EmployeeRequest;
import com.seminar.easyCookWeb.model.user.EmployeeResponse;
import com.seminar.easyCookWeb.pojo.appUser.Employee;
import com.seminar.easyCookWeb.repository.users.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.Authentication;
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
    MemberRepository memberRepository;
    EmployeeMapper mapper;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper mapper, MemberRepository memberRepository){
        this.employeeRepository= employeeRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.memberRepository = memberRepository;
        this.mapper= mapper;
    }


    public Optional<EmployeeResponse> saveEmployee(EmployeeRequest request){
        if(employeeRepository.findByAccount(request.getAccount()).isPresent() || memberRepository.findByAccount(request.getAccount()).isPresent()){
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
                    if(!authentication.getName().equals(it.get().getAccount())){
                        throw new BusinessException("You are not the employee you want to update, so you cannot update this employee");
                    }else if(employeeRepository.ExistAccountandEmail(employeeRequest.getAccount(), employeeRequest.getEmail(), originEmployee.getId()) > 0){
                        throw new EntityCreatedConflictException("this account or email have already in used!");
                    }else{
                        mapper.update(employeeRequest, originEmployee);
                        return originEmployee;
                    }
                })
                .map(employeeRepository::save)
                .map(mapper::toModel);
    }

    /**
     * Admin更新員工腳色
     * @param iid 員工id
     * @return 員工最新樣式
     */
    public Optional<EmployeeResponse> updatebyadmin(Long iid,EmployeeRequest employeeRequest) {
        return Optional.of(employeeRepository.findById(iid))
                .map(it -> {
                        Employee originEmployee = it.orElseThrow(() -> new EntityNotFoundException("Cannot find employee"));
                        originEmployee.setRole(employeeRequest.getRole());
                        return originEmployee;
                })
                .map(employeeRepository::save)
                .map(mapper::toModel);
    }


    public Optional<EmployeeResponse> updatePwd(Long employeeId,UpdatePwd updatePwd, Authentication authentication){
        return Optional.of(employeeRepository.findById(employeeId).get())
                .map(it -> {
                    log.info("change pwd => " + it);
                    if(!authentication.getName().equals(it.getAccount()) &&
                            !authentication.getAuthorities().stream().findFirst().get().toString().equals(Role.ROLE_ADMIN)) {
                        throw new BusinessException("You are not the employee you want to update, so you cannot update this employee");
                    }
                    if(passwordEncoder.matches(updatePwd.getPrepassword(), it.getPassword())){
                        it.setPassword(passwordEncoder.encode(updatePwd.getNewpassword()));
                        return it;
                    }
                    throw new BusinessException("password are not correct");
                })
                .map(employeeRepository::save)
                .map(mapper::toModel);
    }
}
