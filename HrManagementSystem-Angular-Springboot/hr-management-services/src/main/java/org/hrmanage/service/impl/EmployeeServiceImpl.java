package org.hrmanage.service.impl;

import lombok.RequiredArgsConstructor;
import org.hrmanage.dto.EmployeeDto;
import org.hrmanage.dto.EmployeeRegistrationDto;
import org.hrmanage.dto.EmployeeUpdateDto;
import org.hrmanage.entity.EmployeeEntity;
import org.hrmanage.repository.EmployeeRepository;
import org.hrmanage.service.EmployeeService;
import org.hrmanage.util.DepartmentType;
import org.hrmanage.util.Role;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<EmployeeEntity> employeeRepositoryAll = employeeRepository.findAll();
        List<EmployeeDto> employeeDtoList = new ArrayList<>();
        employeeRepositoryAll.forEach(employeeEntity -> employeeDtoList.add(modelMapper.map(employeeEntity, EmployeeDto.class)));
        return employeeDtoList;
    }

    @Override
    public EmployeeDto getEmployeeById(Integer id) {
        if (employeeRepository.existsById(id)) {
            EmployeeEntity employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
            return modelMapper.map(employee, EmployeeDto.class);
        }
        return null;
    }

    @Override
    public EmployeeDto addEmployee(EmployeeDto employeeDto) {
        if (employeeDto.getId() == null) {
            EmployeeEntity savedEntity = employeeRepository.save(modelMapper.map(employeeDto, EmployeeEntity.class));
            return modelMapper.map(savedEntity, EmployeeDto.class);
        }
        return null;
    }

    @Override
    public EmployeeDto addEmployeeWithAuth(EmployeeRegistrationDto employeeRegistrationDto) {
        // Check if username already exists
        if (employeeRepository.existsByUsername(employeeRegistrationDto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        // Check if email already exists
        if (employeeRepository.existsByEmail(employeeRegistrationDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setName(employeeRegistrationDto.getName());
        employeeEntity.setEmail(employeeRegistrationDto.getEmail());
        employeeEntity.setUsername(employeeRegistrationDto.getUsername());
        employeeEntity.setPassword(passwordEncoder.encode(employeeRegistrationDto.getPassword()));
        employeeEntity.setDepartmentType(employeeRegistrationDto.getDepartmentType());
        employeeEntity.setRole(Role.EMPLOYEE);
        
        EmployeeEntity savedEntity = employeeRepository.save(employeeEntity);
        return modelMapper.map(savedEntity, EmployeeDto.class);
    }

    @Override
    public EmployeeDto updateEmployee(Integer id, EmployeeUpdateDto employeeDto) {
        var existingOpt = employeeRepository.findById(id);
        if (existingOpt.isEmpty() || !id.equals(employeeDto.getId())) {
            return null;
        }
        EmployeeEntity existing = existingOpt.get();
        existing.setName(employeeDto.getName());
        existing.setEmail(employeeDto.getEmail());
        existing.setUsername(employeeDto.getUsername());
        if (employeeDto.getPassword() != null && !employeeDto.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(employeeDto.getPassword()));
        }
        existing.setDepartmentType(employeeDto.getDepartmentType());
        existing.setRole(employeeDto.getRole());

        EmployeeEntity saved = employeeRepository.save(existing);
        return modelMapper.map(saved, EmployeeDto.class);
    }

    // Backward-compatible path used by older tests: only updates name/email/department
    @Override
    public EmployeeDto updateEmployee(Integer id, EmployeeDto employeeDto) {
        if (employeeRepository.existsById(id) && id.equals(employeeDto.getId())) {
            EmployeeEntity existing = employeeRepository.findById(id).orElseThrow();
            existing.setName(employeeDto.getName());
            existing.setEmail(employeeDto.getEmail());
            existing.setDepartmentType(employeeDto.getDepartmentType());
            EmployeeEntity saved = employeeRepository.save(existing);
            return modelMapper.map(saved, EmployeeDto.class);
        }
        return null;
    }

    @Override
    public Boolean deleteEmployee(Integer id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return !employeeRepository.existsById(id);
        }
        return false;
    }

}
