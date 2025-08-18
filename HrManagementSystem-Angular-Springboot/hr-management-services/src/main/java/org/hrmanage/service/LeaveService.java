package org.hrmanage.service;

import org.hrmanage.dto.LeaveDto;
import org.hrmanage.dto.LeaveSendDto;

import java.util.List;

public interface LeaveService {
    List<LeaveSendDto> getAllLeaves();

    LeaveSendDto getLeaveById(Integer id);

    LeaveSendDto addLeave(LeaveDto leaveDto);

    LeaveSendDto updateLeave(Integer id, LeaveDto leaveDto);

    Boolean deleteLeave(Integer id);
}
