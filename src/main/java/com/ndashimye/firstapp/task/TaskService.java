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
        List<Task> tasks = taskRepository.findByParentTaskOrderByTodoTask_PositionAsc(task.get());

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
        Optional<Task> task = taskRepository.findById(taskId);

        if(!task.isPresent()){
            throw new TaskNotFoundException();
        }
        if (Objects.isNull(task.get().getTodoTask())) {
            todoTaskRepository.save(todoTask);
            task.get().setTodoTask(todoTask);
            assignPositionToNewTask(task.get());
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

        Optional<Task> task = taskRepository.findById(taskId);

        if(!task.isPresent()){
            throw new TaskNotFoundException();
        }

        if (Objects.nonNull(task.get().getTodoTask())) {
            if (Objects.nonNull(updatedTodoTask.getTodo()) && !updatedTodoTask.getTodo().equals("")) {
                task.get().getTodoTask().setTodo(updatedTodoTask.getTodo());
            }
            if (Objects.nonNull(updatedTodoTask.getCompletionTime()) && !updatedTodoTask.getCompletionTime().equals("")) {
                task.get().getTodoTask().setCompletionTime(updatedTodoTask.getCompletionTime());
            }
            if (Objects.nonNull(updatedTodoTask.getPriorityLevel()) && !String.valueOf(updatedTodoTask.getPriorityLevel()).equals("")) {
                task.get().getTodoTask().setPriorityLevel(updatedTodoTask.getPriorityLevel());
            }
            if (Objects.nonNull(updatedTodoTask.getIsCompleted()) && !String.valueOf(updatedTodoTask.getIsCompleted()).equals("")) {
                task.get().getTodoTask().setIsCompleted(updatedTodoTask.getIsCompleted());
            }
        }
    }

    public void updateTaskPosition(Integer taskId, int newPosition) throws TaskNotFoundException {
        Optional<Task> task = taskRepository.findById(taskId);

        if(!task.isPresent()){
            throw new TaskNotFoundException();
        }

        TodoTask todoTask = task.get().getTodoTask();
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

        Optional<Task> task = taskRepository.findById(taskId);

        if(!task.isPresent()){
            throw new TaskNotFoundException();
        }

        if (Objects.nonNull(task.get().getTodoTask())) {
            todoTaskRepository.delete(task.get().getTodoTask());
        }
    }
}
