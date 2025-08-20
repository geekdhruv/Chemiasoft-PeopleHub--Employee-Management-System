package org.hrmanage.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hrmanage.util.DepartmentType;
import org.hrmanage.util.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUpdateDto {
    @NotNull
    private Integer id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    // Optional: if empty or null, keep previous password
    private String password;

    @NotNull
    private DepartmentType departmentType;

    @NotNull
    private Role role;
}


