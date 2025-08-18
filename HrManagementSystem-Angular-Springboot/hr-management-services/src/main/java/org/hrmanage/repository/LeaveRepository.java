package org.hrmanage.repository;

import org.hrmanage.entity.LeaveEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRepository extends JpaRepository<LeaveEntity, Integer> {
}
