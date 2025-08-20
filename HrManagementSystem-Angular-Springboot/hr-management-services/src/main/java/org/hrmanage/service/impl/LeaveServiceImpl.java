package org.hrmanage.service.impl;

import lombok.RequiredArgsConstructor;
import org.hrmanage.dto.EmployeeDto;
import org.hrmanage.dto.LeaveDto;
import org.hrmanage.dto.LeaveSendDto;
import org.hrmanage.entity.EmployeeEntity;
import org.hrmanage.entity.LeaveEntity;
import org.hrmanage.repository.LeaveRepository;
import org.hrmanage.repository.EmployeeRepository;
import org.hrmanage.service.EmployeeService;
import org.hrmanage.service.LeaveService;
import org.hrmanage.util.LeaveStatus;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRepository leaveRepository;
    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<LeaveSendDto> getAllLeaves() {
        List<LeaveEntity> allLeaves = leaveRepository.findAll();
        List<EmployeeDto> allEmployees = employeeService.getAllEmployees();
        List<LeaveSendDto> dtoList = new ArrayList<>();

        for (LeaveEntity leave : allLeaves) {
            LeaveDto leaveDto = modelMapper.map(leave, LeaveDto.class);
            for (EmployeeDto employee : allEmployees) {
                if (employee.getId().equals(leaveDto.getEmployeeId())) {
                    dtoList.add(getLeaveSendDto(leaveDto, employee));
                }
            }
        }
        return dtoList;
    }

    @Override
    public List<LeaveSendDto> getLeavesForCurrentUser(String username) {
        var employee = employeeRepository.findByUsername(username);
        if (employee == null) {
            return new ArrayList<>();
        }
        List<LeaveEntity> leaves = leaveRepository.findByEmployee_Id(employee.getId());
        List<LeaveSendDto> dtoList = new ArrayList<>();
        for (LeaveEntity leave : leaves) {
            LeaveDto leaveDto = modelMapper.map(leave, LeaveDto.class);
            leaveDto.setEmployeeId(employee.getId());
            EmployeeDto employeeDto = employeeService.getEmployeeById(employee.getId());
            dtoList.add(getLeaveSendDto(leaveDto, employeeDto));
        }
        return dtoList;
    }

    @Override
    public LeaveSendDto getLeaveById(Integer id) {
        Optional<LeaveEntity> leaveOpt = leaveRepository.findById(id);
        if (leaveOpt.isPresent()) {
            LeaveEntity entity = leaveOpt.get();
            LeaveDto leaveDto = modelMapper.map(entity, LeaveDto.class);
            leaveDto.setEmployeeId(entity.getEmployee().getId());
            EmployeeDto employeeDto = employeeService.getEmployeeById(entity.getEmployee().getId());
            return getLeaveSendDto(leaveDto, employeeDto);
        }
        return null;
    }

    @Override
    public LeaveSendDto addLeave(LeaveDto leaveDto) {
        System.out.println(leaveDto.toString());
        if (leaveDto.getId() == null) {
            LeaveDto saved = modelMapper.map(leaveRepository.save(modelMapper.map(leaveDto, LeaveEntity.class)), LeaveDto.class);
            EmployeeDto employee = employeeService.getEmployeeById(leaveDto.getEmployeeId());
            return getLeaveSendDto(saved, employee);
        }
        return null;
    }

    @Override
    public LeaveSendDto updateLeave(Integer id, LeaveDto leaveDto) {
        if (id == null || leaveDto.getId() == null) {
            return null;
        }
        if (leaveDto.getId().equals(id) && leaveRepository.existsById(id)) {
            LeaveEntity existing = leaveRepository.findById(id).orElseThrow();
            // Map updatable fields
            existing.setLeaveType(leaveDto.getLeaveType());
            existing.setStartDate(leaveDto.getStartDate());
            existing.setEndDate(leaveDto.getEndDate());
            existing.setReason(leaveDto.getReason());
            existing.setStatus(leaveDto.getStatus());
            LeaveEntity savedEntity = leaveRepository.save(existing);
            LeaveDto saved = modelMapper.map(savedEntity, LeaveDto.class);
            saved.setEmployeeId(savedEntity.getEmployee().getId());
            EmployeeDto employee = employeeService.getEmployeeById(saved.getEmployeeId());
            return getLeaveSendDto(saved, employee);
        }
        return null;
    }

    @Override
    public Boolean deleteLeave(Integer id) {
        if (leaveRepository.existsById(id)) {
            leaveRepository.deleteById(id);
            return !leaveRepository.existsById(id);
        }
        return false;
    }

    @Override
    public Boolean deleteOwnLeave(Integer id, String username) {
        var employee = employeeRepository.findByUsername(username);
        if (employee == null) return false;
        Optional<LeaveEntity> opt = leaveRepository.findById(id);
        if (opt.isPresent() && opt.get().getEmployee().getId().equals(employee.getId())) {
            leaveRepository.deleteById(id);
            return !leaveRepository.existsById(id);
        }
        return false;
    }

    @Override
    public LeaveSendDto approveLeave(Integer id) {
        Optional<LeaveEntity> leaveOpt = leaveRepository.findById(id);
        if (leaveOpt.isPresent()) {
            LeaveEntity leave = leaveOpt.get();
            leave.setStatus(LeaveStatus.APPROVED);
            LeaveEntity savedLeave = leaveRepository.save(leave);
            LeaveDto leaveDto = modelMapper.map(savedLeave, LeaveDto.class);
            leaveDto.setEmployeeId(savedLeave.getEmployee().getId());
            EmployeeDto employeeDto = employeeService.getEmployeeById(savedLeave.getEmployee().getId());
            return getLeaveSendDto(leaveDto, employeeDto);
        }
        return null;
    }

    @Override
    public LeaveSendDto rejectLeave(Integer id) {
        Optional<LeaveEntity> leaveOpt = leaveRepository.findById(id);
        if (leaveOpt.isPresent()) {
            LeaveEntity leave = leaveOpt.get();
            leave.setStatus(LeaveStatus.REJECTED);
            LeaveEntity savedLeave = leaveRepository.save(leave);
            LeaveDto leaveDto = modelMapper.map(savedLeave, LeaveDto.class);
            leaveDto.setEmployeeId(savedLeave.getEmployee().getId());
            EmployeeDto employeeDto = employeeService.getEmployeeById(savedLeave.getEmployee().getId());
            return getLeaveSendDto(leaveDto, employeeDto);
        }
        return null;
    }

    private LeaveSendDto getLeaveSendDto(LeaveDto leaveDto, EmployeeDto employeeDto) {
        return new LeaveSendDto(
                leaveDto.getId(),
                employeeDto,
                leaveDto.getLeaveType(),
                leaveDto.getStartDate(),
                leaveDto.getEndDate(),
                leaveDto.getReason(),
                leaveDto.getStatus(),
                leaveDto.getCreatedAt(),
                leaveDto.getUpdatedAt()
        );
    }
}