package com.ndashimye.firstapp.task;

import com.ndashimye.firstapp.todo.Todo;
import com.ndashimye.firstapp.todotask.TodoTask;
import com.ndashimye.firstapp.todotask.TodoTaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TodoTaskRepository todoTaskRepository;

    public Task getTaskById(Integer taskId) throws TaskNotFoundException {

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException());

        return task;
    }


    public List<Task> getAllChildTasksByTaskId(Integer taskId) throws TaskNotFoundException {

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException());
        List<Task> tasks = taskRepository.findByParentTaskOrderByTodoTask_PositionAsc(task);

        return tasks;
    }


    public List<Task> getAllChildTasksByTaskIdOrderedByPriority(Integer taskId) throws TaskNotFoundException {

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException());
        List<Task> tasks = taskRepository.findByParentTaskOrderByTodoTask_PriorityLevelDesc(task);

        return tasks;
    }

    public List<Task> getCompletedChildTasks(Integer taskId) throws TaskNotFoundException {

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException());
        List<Task> tasks = taskRepository.findByCompletedChildTasks(task);

        return tasks;
    }

    public List<Task> getUncompletedChildTasks(Integer taskId) throws TaskNotFoundException {

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException());
        List<Task> tasks = taskRepository.findByUncompletedChildTasks(task);

        return tasks;
    }



    public void addNewTask(Task task) {
        taskRepository.save(task);
    }

    public void updateTask(Task updatedTask, Task task) {

//        if(Objects.nonNull(updatedTask.getTodoTask())) {
//            if(!updatedTask.getTodoTask().equals("")) {
//                task.setTodoTask(updatedTask.getTodoTask());
//            }
//        }else {
//            todoTaskRepository.delete(task.getTodoTask());
//        }


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



    public void addNewTodoTask(Integer taskId, TodoTask todoTask) throws TaskNotFoundException {

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException());

        if (Objects.isNull(task.getTodoTask())) {
            todoTaskRepository.save(todoTask);
            task.setTodoTask(todoTask);
            assignPositionToNewTask(task);
        }
    }

    public void assignPositionToNewTask(Task task) {
        if (task.getParentTask() == null) {
            TodoTask lastTask = todoTaskRepository.findTopByTodoOrderByPositionDesc(task.getTodoTask().getTodo());
            if (lastTask == null) {
                task.getTodoTask().setPosition(1);
            } else {
                task.getTodoTask().setPosition(lastTask.getPosition() + 1);
            }
        } else {
            List<Task> childTasks = task.getParentTask().getChildTasks();
            if (childTasks.isEmpty()) {
                task.getTodoTask().setPosition(1);
            } else {
                Task lastChildTask = childTasks.get(childTasks.size() - 1);
                task.getTodoTask().setPosition(lastChildTask.getTodoTask().getPosition() + 1);
            }
        }
    }


    public void updateTodoTask(Integer taskId, TodoTask updatedTodoTask) throws TaskNotFoundException {

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException());

        if (Objects.nonNull(task.getTodoTask())) {
            if (Objects.nonNull(updatedTodoTask.getTodo()) && !updatedTodoTask.getTodo().equals("")) {
                task.getTodoTask().setTodo(updatedTodoTask.getTodo());
            }
            if (Objects.nonNull(updatedTodoTask.getCompletionTime()) && !updatedTodoTask.getCompletionTime().equals("")) {
                task.getTodoTask().setCompletionTime(updatedTodoTask.getCompletionTime());
            }
            if (Objects.nonNull(updatedTodoTask.getPriorityLevel()) && !String.valueOf(updatedTodoTask.getPriorityLevel()).equals("")) {
                task.getTodoTask().setPriorityLevel(updatedTodoTask.getPriorityLevel());
            }
            if (Objects.nonNull(updatedTodoTask.getIsCompleted()) && !String.valueOf(updatedTodoTask.getIsCompleted()).equals("")) {
                task.getTodoTask().setIsCompleted(updatedTodoTask.getIsCompleted());
            }
        }
    }

    public void updateTaskPosition(Integer taskId, int newPosition) throws TaskNotFoundException {

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException());

        TodoTask todoTask = task.getTodoTask();
        Todo todo = todoTask.getTodo();
        int currentPosition = todoTask.getPosition();
        if (newPosition > currentPosition) {
            List<TodoTask> tasksToUpdate = todoTaskRepository.findByTodoAndPositionBetweenOrderByPositionAsc(todo, currentPosition + 1, newPosition);
            for (TodoTask taskToUpdate : tasksToUpdate) {
                taskToUpdate.setPosition(taskToUpdate.getPosition() - 1);
            }
        } else if (newPosition < currentPosition) {
            List<TodoTask> tasksToUpdate = todoTaskRepository.findByTodoAndPositionBetweenOrderByPositionDesc(todo, newPosition, currentPosition - 1);
            for (TodoTask taskToUpdate : tasksToUpdate) {
                taskToUpdate.setPosition(taskToUpdate.getPosition() + 1);
            }
        }
        todoTask.setPosition(newPosition);
    }


    public void deleteTodoTask(Integer taskId) throws TaskNotFoundException {

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException());

        if (Objects.nonNull(task.getTodoTask())) {
            todoTaskRepository.delete(task.getTodoTask());
        }
    }
}
