package com.seminar.easyCookWeb.mapper;

import com.seminar.easyCookWeb.model.user.EmployeeRequest;
import com.seminar.easyCookWeb.model.user.EmployeeResponse;
import com.seminar.easyCookWeb.pojo.appUser.Employee;
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
