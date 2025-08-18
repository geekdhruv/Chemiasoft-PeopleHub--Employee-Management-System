package org.hrmanage.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hrmanage.dto.EmployeeDto;
import org.hrmanage.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        return ResponseEntity.ok(employeeService.addEmployee(employeeDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable("id") Integer id, @Valid @RequestBody EmployeeDto employeeDto) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employeeDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteEmployee(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(employeeService.deleteEmployee(id));
    }

    @GetMapping("/report")
    public void exportCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=employees.csv");

        List<EmployeeDto> employees = employeeService.getAllEmployees();
        PrintWriter writer = response.getWriter();

        writer.println("ID,Name,Email,Department,Created At,Updated At");

        for (EmployeeDto emp : employees) {
            writer.println(String.format("%d,%s,%s,%s,%s,%s",
                    emp.getId(),
                    emp.getName(),
                    emp.getEmail(),
                    emp.getDepartmentType(),
                    emp.getCreatedAt(),
                    emp.getUpdatedAt()));
        }

        writer.flush();
        writer.close();
    }
}
