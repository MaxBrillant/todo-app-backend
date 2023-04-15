package com.ndashimye.firstapp.task;

import com.ndashimye.firstapp.todo.Todo;
import com.ndashimye.firstapp.todo.TodoNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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


    public List<Task> getAllChildTasksByTaskId(Integer taskId) throws TaskNotFoundException {

        Optional<Task> task = taskRepository.findById(taskId);

        if(!task.isPresent()){
            throw new TaskNotFoundException();
        }
        List<Task> tasks = taskRepository.findByParentTaskOrderByTodoTask_OrderAsc(task.get());

        return tasks;
    }


    public List<Task> getAllChildTasksByTaskIdOrderedByPriority(Integer taskId) throws TaskNotFoundException {

        Optional<Task> task = taskRepository.findById(taskId);

        if(!task.isPresent()){
            throw new TaskNotFoundException();
        }
        List<Task> tasks = taskRepository.findByParentTaskOrderByTodoTask_PriorityLevelDesc(task.get());

        return tasks;
    }

    public List<Task> getCompletedChildTasks(Integer taskId) throws TaskNotFoundException {
        Optional<Task> task = taskRepository.findById(taskId);

        if(!task.isPresent()){
            throw new TaskNotFoundException();
        }
        List<Task> tasks = taskRepository.findByCompletedChildTasks(task.get());

        return tasks;
    }

    public List<Task> getUncompletedChildTasks(Integer taskId) throws TaskNotFoundException {
        Optional<Task> task = taskRepository.findById(taskId);

        if(!task.isPresent()){
            throw new TaskNotFoundException();
        }
        List<Task> tasks = taskRepository.findByUncompletedChildTasks(task.get());

        return tasks;
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


        if(Objects.nonNull(updatedTask.getParentTask())) {
            if(!updatedTask.getParentTask().equals("")) {
                task.setParentTask(updatedTask.getParentTask());
            }
        }else {
            task.setParentTask(updatedTask.getParentTask());
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
