package org.hrmanage.service;

import org.hrmanage.dto.EmployeeDto;
import org.hrmanage.util.DepartmentType;

import java.util.List;

@SuppressWarnings("unused")
public interface EmployeeService {

    List<EmployeeDto> getAllEmployees();

    EmployeeDto getEmployeeById(Integer id);

    EmployeeDto addEmployee(EmployeeDto employeeDto);

    EmployeeDto updateEmployee(Integer id, EmployeeDto employeeDto);

    Boolean deleteEmployee(Integer id);
}
