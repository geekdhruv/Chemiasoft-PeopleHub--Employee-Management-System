package org.hrmanage.service;

import org.hrmanage.dto.EmployeeDto;
import org.hrmanage.dto.EmployeeRegistrationDto;
import org.hrmanage.dto.EmployeeUpdateDto;
import org.hrmanage.util.DepartmentType;

import java.util.List;

@SuppressWarnings("unused")
public interface EmployeeService {

    List<EmployeeDto> getAllEmployees();

    EmployeeDto getEmployeeById(Integer id);

    EmployeeDto addEmployee(EmployeeDto employeeDto);

    EmployeeDto addEmployeeWithAuth(EmployeeRegistrationDto employeeRegistrationDto);

    EmployeeDto updateEmployee(Integer id, EmployeeUpdateDto employeeDto);

    // Backward-compatible overload used by existing tests (updates basic fields only)
    EmployeeDto updateEmployee(Integer id, EmployeeDto employeeDto);

    Boolean deleteEmployee(Integer id);
}
