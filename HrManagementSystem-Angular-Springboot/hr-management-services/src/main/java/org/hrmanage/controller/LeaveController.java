package org.hrmanage.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hrmanage.dto.LeaveDto;
import org.hrmanage.dto.LeaveSendDto;
import org.hrmanage.service.LeaveService;
import org.hrmanage.repository.EmployeeRepository;
import org.hrmanage.entity.EmployeeEntity;
import org.hrmanage.util.LeaveStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("api/leave")
public class LeaveController {

    private final LeaveService leaveService;
    private final EmployeeRepository employeeRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<LeaveSendDto>> getAll(Authentication authentication) {
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.ok(leaveService.getAllLeaves());
        }
        // Employee: return only their own
        String username = authentication.getName();
        return ResponseEntity.ok(leaveService.getLeavesForCurrentUser(username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveSendDto> getById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(leaveService.getLeaveById(id));
    }

    @PostMapping("/apply")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<LeaveSendDto> createLeave(@Valid @RequestBody LeaveDto leaveDto, Authentication authentication) {
        String username = authentication.getName();
        EmployeeEntity employee = employeeRepository.findByUsername(username);
        if (employee == null) {
            return ResponseEntity.badRequest().build();
        }
        // Force linkage and default status
        leaveDto.setEmployeeId(employee.getId());
        leaveDto.setStatus(LeaveStatus.PENDING);
        return ResponseEntity.ok(leaveService.addLeave(leaveDto));
    }

    @PutMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LeaveSendDto> updateLeave(@PathVariable("id") Integer id, @Valid @RequestBody LeaveDto leaveDto) {
        return ResponseEntity.ok(leaveService.updateLeave(id, leaveDto));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Boolean> deleteLeave(@PathVariable("id") Integer id, Authentication authentication) {
        return ResponseEntity.ok(leaveService.deleteOwnLeave(id, authentication.getName()));
    }

    @PutMapping("/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LeaveSendDto> approveLeave(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(leaveService.approveLeave(id));
    }

    @PutMapping("/reject/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LeaveSendDto> rejectLeave(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(leaveService.rejectLeave(id));
    }

    @GetMapping("/report")
    public void exportCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=leaves.csv");

        List<LeaveSendDto> leaves = leaveService.getAllLeaves();
        PrintWriter writer = response.getWriter();

        writer.println("ID,Employee Name,Email,Department,Leave Type,Start Date,End Date,Reason,Status,Created At,Updated At");

        for (LeaveSendDto leave : leaves) {
            writer.println(String.format("%d,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                    leave.getId(),
                    leave.getEmployee().getName(),
                    leave.getEmployee().getEmail(),
                    leave.getEmployee().getDepartmentType(),
                    leave.getLeaveType(),
                    leave.getStartDate(),
                    leave.getEndDate(),
                    leave.getReason(),
                    leave.getStatus(),
                    leave.getCreatedAt(),
                    leave.getUpdatedAt()
            ));
        }

        writer.flush();
        writer.close();
    }
}
