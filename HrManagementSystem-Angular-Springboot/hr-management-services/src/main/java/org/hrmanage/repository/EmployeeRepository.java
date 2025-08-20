package org.hrmanage.repository;

import jakarta.validation.constraints.NotNull;
import org.hrmanage.entity.EmployeeEntity;
import org.hrmanage.util.DepartmentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {

    List<EmployeeEntity> findEmployeesByDepartmentType(@NotNull(message = "Department must be insert") DepartmentType departmentType);
    
    EmployeeEntity findByUsername(String username);
    EmployeeEntity findByName(String name);
    EmployeeEntity findByNameIgnoreCase(String name);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}
//jpa