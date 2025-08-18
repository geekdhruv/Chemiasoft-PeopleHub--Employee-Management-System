package org.hrmanage.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hrmanage.dto.LeaveDto;
import org.hrmanage.dto.LeaveSendDto;
import org.hrmanage.service.LeaveService;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<List<LeaveSendDto>> getAll() {
        return ResponseEntity.ok(leaveService.getAllLeaves());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveSendDto> getById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(leaveService.getLeaveById(id));
    }

    @PostMapping
    public ResponseEntity<LeaveSendDto> createLeave(@Valid @RequestBody LeaveDto leaveDto) {
        return ResponseEntity.ok(leaveService.addLeave(leaveDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeaveSendDto> updateLeave(@PathVariable("id") Integer id, @Valid @RequestBody LeaveDto leaveDto) {
        return ResponseEntity.ok(leaveService.updateLeave(id, leaveDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteLeave(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(leaveService.deleteLeave(id));
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
