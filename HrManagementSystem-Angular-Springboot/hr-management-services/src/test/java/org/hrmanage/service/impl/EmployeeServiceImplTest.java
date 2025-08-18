package org.hrmanage.service.impl;

import org.hrmanage.dto.EmployeeDto;
import org.hrmanage.entity.EmployeeEntity;
import org.hrmanage.repository.EmployeeRepository;
import org.hrmanage.util.DepartmentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Mock
    private ModelMapper modelMapper;

    private EmployeeDto sampleEmployeeDto;
    private EmployeeEntity sampleEmployeeEntity;

    @DisplayName("Set up sample employee data")
    @BeforeEach
    void setUp() {
        sampleEmployeeEntity = new EmployeeEntity(
                1,
                "John Doe",
                "johndoe@example.com",
                DepartmentType.IT,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 1)
        );

        sampleEmployeeDto = new EmployeeDto(
                1,
                "John Doe",
                "johndoe@example.com",
                DepartmentType.IT,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 1)
        );
    }

    @Test
    @DisplayName("Get All Employees Success")
    void getAllEmployees() {
        List<EmployeeEntity> employeeList = List.of(sampleEmployeeEntity);
        when(employeeRepository.findAll()).thenReturn(employeeList);
        when(modelMapper.map(sampleEmployeeEntity, EmployeeDto.class)).thenReturn(sampleEmployeeDto);

        List<EmployeeDto> result = employeeService.getAllEmployees();

        assertEquals(1, result.size(), "The size of the employee list should be 1");
        assertEquals(sampleEmployeeDto, result.get(0), "The employee DTO should match the expected DTO");
    }

    @Test
    @DisplayName("Get Employee By Id Success")
    void getEmployeeById() {
        when(employeeRepository.existsById(1)).thenReturn(true);
        when(employeeRepository.findById(1)).thenReturn(Optional.of(sampleEmployeeEntity));
        when(modelMapper.map(sampleEmployeeEntity, EmployeeDto.class)).thenReturn(sampleEmployeeDto);

        EmployeeDto result = employeeService.getEmployeeById(1);

        assertEquals(sampleEmployeeDto, result, "The employee DTO should match the expected DTO");
    }

    @Test
    @DisplayName("Add Employee Success")
    void addEmployee() {
        EmployeeDto newEmployeeDto = new EmployeeDto(null, "Jane Doe", "janedoe@example.com", DepartmentType.HR, LocalDate.now(), LocalDate.now());
        EmployeeEntity newEmployeeEntity = new EmployeeEntity(1, "Jane Doe", "janedoe@example.com", DepartmentType.HR, LocalDate.now(), LocalDate.now());

        when(modelMapper.map(newEmployeeDto, EmployeeEntity.class)).thenReturn(newEmployeeEntity);
        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(newEmployeeEntity);
        when(modelMapper.map(newEmployeeEntity, EmployeeDto.class)).thenReturn(newEmployeeDto);

        EmployeeDto result = employeeService.addEmployee(newEmployeeDto);

        assertEquals(newEmployeeDto, result, "The added employee DTO should match the expected DTO");
    }

    @Test
    @DisplayName("Update Employee Success")
    void updateEmployee() {
        EmployeeDto updatedEmployeeDto = new EmployeeDto(1, "Jane Doe", "janedoe@example.com", DepartmentType.HR, LocalDate.now(), LocalDate.now());
        EmployeeEntity updatedEmployeeEntity = new EmployeeEntity(1, "Jane Doe", "janedoe@example.com", DepartmentType.HR, LocalDate.now(), LocalDate.now());

        when(employeeRepository.existsById(1)).thenReturn(true);
        when(modelMapper.map(updatedEmployeeDto, EmployeeEntity.class)).thenReturn(updatedEmployeeEntity);
        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(updatedEmployeeEntity);
        when(modelMapper.map(updatedEmployeeEntity, EmployeeDto.class)).thenReturn(updatedEmployeeDto);

        EmployeeDto result = employeeService.updateEmployee(1, updatedEmployeeDto);

        assertEquals(updatedEmployeeDto, result, "The updated employee DTO should match the expected DTO");
    }

    @Test
    @DisplayName("Delete Employee Success")
    void deleteEmployee() {
        when(employeeRepository.existsById(1))
                .thenReturn(true)
                .thenReturn(false);

        Boolean result = employeeService.deleteEmployee(1);

        assertTrue(result, "The employee should be deleted successfully");
    }
}
