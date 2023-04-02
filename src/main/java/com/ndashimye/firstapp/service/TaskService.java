package com.ndashimye.firstapp.service;

import com.ndashimye.firstapp.error.TaskNotFoundException;
import com.ndashimye.firstapp.model.Task;
import com.ndashimye.firstapp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task getTaskById(Integer taskId) throws TaskNotFoundException {

        Optional<Task> task = taskRepository.findById(taskId);

        if(!task.isPresent()){
            throw new TaskNotFoundException();
        }
        return task.get();

    }

    public void addNewTask(Task task) {
        taskRepository.save(task);
    }

    public void updateTask(Task updatedTask, Task task) {

        if(Objects.nonNull(updatedTask.getTodoTask())) {
            if(!updatedTask.getTodoTask().equals("")) {
                task.setTodoTask(updatedTask.getTodoTask());
            }
        }else {
            task.setTodoTask(updatedTask.getTodoTask());
        }

        if (Objects.nonNull(updatedTask.getName()) && !updatedTask.getName().equals("")) {
            task.setName(updatedTask.getName());
        }

        taskRepository.save(task);
    }

    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }
}
