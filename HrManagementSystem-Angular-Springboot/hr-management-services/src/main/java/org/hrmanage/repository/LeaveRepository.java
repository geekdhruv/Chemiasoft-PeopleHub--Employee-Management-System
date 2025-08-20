package org.hrmanage.repository;

import org.hrmanage.entity.LeaveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LeaveRepository extends JpaRepository<LeaveEntity, Integer> {
    List<LeaveEntity> findByEmployee_Id(Integer employeeId);
}
