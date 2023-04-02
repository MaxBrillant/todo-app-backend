package com.ndashimye.firstapp.controller;

import com.ndashimye.firstapp.error.TodayTaskNotFoundException;
import com.ndashimye.firstapp.model.TodayTask;
import com.ndashimye.firstapp.service.TodayTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/today-tasks")
public class TodayTaskController {

    @Autowired
    private TodayTaskService todayTaskService;

    @GetMapping("/{todayTaskId}")
    public TodayTask getTodayTaskById(@PathVariable Integer todayTaskId)
            throws TodayTaskNotFoundException {

        return todayTaskService.getTodayTaskById(todayTaskId);
    }

    @PostMapping()
    public String addTodayTask(@RequestBody TodayTask todayTask) {

        todayTaskService.addNewTodayTask(todayTask);

        return "today task of id "+todayTask.getTodayTaskId()+" was added successfully";
    }

    @PutMapping("/{todayTaskId}")
    public String updateTodayTask(@RequestBody TodayTask updatedTodayTask,
                                 @PathVariable Integer todayTaskId) throws TodayTaskNotFoundException {

        TodayTask todayTask = todayTaskService.getTodayTaskById(todayTaskId);

        todayTaskService.updateTodayTask(updatedTodayTask, todayTask);

        return "today task of id "+todayTask.getTodayTaskId()+" was updated successfully";
    }

    @DeleteMapping("/{todayTaskId}")
    public String deleteTodayTask(@PathVariable Integer todayTaskId) throws TodayTaskNotFoundException {

        TodayTask todayTask = todayTaskService.getTodayTaskById(todayTaskId);
        int id = todayTask.getTodayTaskId();
        todayTaskService.deleteTodayTask(todayTask);
        return "today task of id "+id+" was successfully deleted from the database";
    }
}
