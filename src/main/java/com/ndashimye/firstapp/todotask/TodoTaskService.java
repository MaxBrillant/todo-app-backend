package com.ndashimye.firstapp.todotask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Objects;
import java.util.Optional;

@Service
public class TodoTaskService {

    @Autowired
    TodoTaskRepository todoTaskRepository;

    public TodoTask getTodoTaskById(Integer todoTaskId) throws TodoTaskNotFoundException {

        Optional<TodoTask> todoTask = todoTaskRepository.findById(todoTaskId);

        if(!todoTask.isPresent()){
            throw new TodoTaskNotFoundException();
        }
        return todoTask.get();

    }

    public void addNewTodoTask(TodoTask todoTask) {
        //set userTodo order
        todoTaskRepository.save(todoTask);
    }

    public void updateTodoTask(TodoTask updatedTodoTask, TodoTask todoTask) {

        if (Objects.nonNull(updatedTodoTask.getTodo()) && !updatedTodoTask.getTodo().equals("")) {
            todoTask.setTodo(updatedTodoTask.getTodo());
        }
        if (Objects.nonNull(updatedTodoTask.getCompletionTime()) && !updatedTodoTask.getCompletionTime().equals("")) {
            todoTask.setCompletionTime(updatedTodoTask.getCompletionTime());
        }
        if (Objects.nonNull(updatedTodoTask.getOrder()) && !String.valueOf(updatedTodoTask.getOrder()).equals("")) {
            todoTask.setOrder(updatedTodoTask.getOrder());
        }
        if (Objects.nonNull(updatedTodoTask.getPriorityLevel()) && !String.valueOf(updatedTodoTask.getPriorityLevel()).equals("")) {
            todoTask.setPriorityLevel(updatedTodoTask.getPriorityLevel());
        }
        if (Objects.nonNull(updatedTodoTask.getCompleted()) && !String.valueOf(updatedTodoTask.getCompleted()).equals("")) {
            todoTask.setCompleted(updatedTodoTask.getCompleted());
        }
        todoTaskRepository.save(todoTask);
    }

    public void deleteTodoTask(TodoTask todoTask) {
        todoTaskRepository.delete(todoTask);
    }
}
