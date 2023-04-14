package com.ndashimye.firstapp.error;
import com.ndashimye.firstapp.task.TaskNotFoundException;
import com.ndashimye.firstapp.todo.TodoNotFoundException;
import com.ndashimye.firstapp.todotask.TodoTaskNotFoundException;
import com.ndashimye.firstapp.user.UserNotFoundException;
import com.ndashimye.firstapp.userprofile.UserProfileNotFoundException;
import com.ndashimye.firstapp.usersettings.UserSettingsNotFoundException;
import com.ndashimye.firstapp.usertodo.UserTodoNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@ResponseStatus
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessage> userNotFoundException(UserNotFoundException exception,
                                                              WebRequest request) {
        ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND,
                exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(message);
    }
    @ExceptionHandler(InvalidTimeFormatException.class)
    public ResponseEntity<ErrorMessage> invalidTimeFormatException(InvalidTimeFormatException exception,
                                                                      WebRequest request) {
        ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST,
                exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(message);
    }

    @ExceptionHandler(UserProfileNotFoundException.class)
    public ResponseEntity<ErrorMessage> userProfileNotFoundException(UserProfileNotFoundException exception,
                                                              WebRequest request) {
        ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND,
                exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(message);
    }

    @ExceptionHandler(UserSettingsNotFoundException.class)
    public ResponseEntity<ErrorMessage> userSettingsNotFoundException(UserSettingsNotFoundException exception,
                                                                     WebRequest request) {
        ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND,
                exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(message);
    }

    @ExceptionHandler(TodoNotFoundException.class)
    public ResponseEntity<ErrorMessage> todoNotFoundException(TodoNotFoundException exception,
                                                                     WebRequest request) {
        ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND,
                exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(message);
    }

    @ExceptionHandler(UserTodoNotFoundException.class)
    public ResponseEntity<ErrorMessage> userTodoNotFoundException(UserTodoNotFoundException exception,
                                                              WebRequest request) {
        ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND,
                exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(message);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorMessage> taskNotFoundException(TaskNotFoundException exception,
                                                                  WebRequest request) {
        ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND,
                exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(message);
    }

    @ExceptionHandler(TodoTaskNotFoundException.class)
    public ResponseEntity<ErrorMessage> todoTaskNotFoundException(TodoTaskNotFoundException exception,
                                                              WebRequest request) {
        ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND,
                exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(message);
    }
}
