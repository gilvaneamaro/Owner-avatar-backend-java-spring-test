package com.teste.selaz.repository;

import com.teste.selaz.entity.Task;
import com.teste.selaz.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    public List<Task> findTaskByStatus(Status status);
}
