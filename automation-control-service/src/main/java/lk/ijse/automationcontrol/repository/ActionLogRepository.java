package lk.ijse.automationcontrol.repository;

import lk.ijse.automationcontrol.model.ActionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionLogRepository extends JpaRepository<ActionLog, String> {
}