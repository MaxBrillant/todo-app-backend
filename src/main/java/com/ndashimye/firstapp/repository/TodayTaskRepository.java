package com.ndashimye.firstapp.repository;

import com.ndashimye.firstapp.model.Task;
import com.ndashimye.firstapp.model.TodayTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface TodayTaskRepository extends JpaRepository<TodayTask, Integer> {
}
