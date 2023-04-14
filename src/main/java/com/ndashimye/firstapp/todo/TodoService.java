package com.ndashimye.firstapp.todo;
import com.ndashimye.firstapp.task.Task;
import com.ndashimye.firstapp.task.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TaskRepository taskRepository;

    public Todo getTodoById(Integer todoId) throws TodoNotFoundException {

        Optional<Todo> todo = todoRepository.findById(todoId);

        if(!todo.isPresent()){
            throw new TodoNotFoundException();
        }
        return todo.get();

    }


    public List<Task> getAllTasksByTodoId(Integer todoId) throws TodoNotFoundException {

        Optional<Todo> todo = todoRepository.findById(todoId);

        if(!todo.isPresent()){
            throw new TodoNotFoundException();
        }
        List<Task> tasks = taskRepository.findByTodoTask_TodoOrderByTodoTask_OrderAsc(todo.get());

        return tasks;
    }


    public List<Task> getAllTasksByTodoIdOrderedByPriority(Integer todoId) throws TodoNotFoundException {

        Optional<Todo> todo = todoRepository.findById(todoId);

        if(!todo.isPresent()){
            throw new TodoNotFoundException();
        }
        List<Task> tasks = taskRepository.findByTodoTask_TodoOrderByTodoTask_PriorityLevelDesc(todo.get());

        return tasks;
    }

    public List<Task> getCompletedTasks(Integer todoId) throws TodoNotFoundException {
        Optional<Todo> todo = todoRepository.findById(todoId);

        if(!todo.isPresent()){
            throw new TodoNotFoundException();
        }
        List<Task> tasks = taskRepository.findByCompletedTasks(todo.get());

        return tasks;
    }

    public List<Task> getUncompletedTasks(Integer todoId) throws TodoNotFoundException {
        Optional<Todo> todo = todoRepository.findById(todoId);

        if(!todo.isPresent()){
            throw new TodoNotFoundException();
        }
        List<Task> tasks = taskRepository.findByUncompletedTasks(todo.get());

        return tasks;
    }

    public void addNewTodo(Todo todo) {
        todoRepository.save(todo);
    }

    public void updateTodo(Todo updatedTodo, Todo todo) {

        if(Objects.nonNull(updatedTodo.getUserTodo())) {
            if(!updatedTodo.getUserTodo().equals("")) {
                todo.setUserTodo(updatedTodo.getUserTodo());
            }
        }else {
            todo.setUserTodo(updatedTodo.getUserTodo());
        }

        if (Objects.nonNull(updatedTodo.getName()) && !updatedTodo.getName().equals("")) {
            todo.setName(updatedTodo.getName());
        }
        if (Objects.nonNull(updatedTodo.getDescription()) && !updatedTodo.getDescription().equals("")) {
            todo.setDescription(updatedTodo.getDescription());
        }
        if (Objects.nonNull(updatedTodo.getDueTime()) && !updatedTodo.getDueTime().equals("")) {
            todo.setDueTime(updatedTodo.getDueTime());
        }
        if (Objects.nonNull(updatedTodo.getIsRecurrent()) && !String.valueOf(updatedTodo.getIsRecurrent()).equals("")) {
            todo.setIsRecurrent(updatedTodo.getIsRecurrent());
        }

        todoRepository.save(todo);
    }

    public void deleteTodo(Todo todo) {
        todoRepository.delete(todo);
    }
}
