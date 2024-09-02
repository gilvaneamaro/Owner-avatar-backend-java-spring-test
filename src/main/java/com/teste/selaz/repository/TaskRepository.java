package com.teste.selaz.repository;

import com.teste.selaz.entity.Task;
import com.teste.selaz.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findTaskByUserId(Long userId);

    @Query(value = "SELECT * FROM task WHERE user_id = :userId ORDER BY due_date ASC", nativeQuery = true)
    List<Task> findAllTasksOrderedByDueDate(@Param("userId") Long userId);

}
