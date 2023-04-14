package com.ndashimye.firstapp.todaytask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodayTaskRepository extends JpaRepository<TodayTask, Integer> {
}
