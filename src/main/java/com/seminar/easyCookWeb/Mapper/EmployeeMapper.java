package com.seminar.easyCookWeb.Mapper;

import com.seminar.easyCookWeb.Entity.User.EmployeeRequest;
import com.seminar.easyCookWeb.Entity.User.EmployeeResponse;
import com.seminar.easyCookWeb.Pojo.appUser.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    EmployeeMapper MAPPER = Mappers.getMapper( EmployeeMapper.class );

    EmployeeResponse toModel(Employee employee);

    Employee toPOJO(EmployeeRequest request);

    List<EmployeeResponse> toModels(List<Employee> employees);
}
