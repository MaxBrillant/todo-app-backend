package com.ndashimye.firstapp.taskGeneration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.task.TaskDTO;
import com.ndashimye.firstapp.task.TaskService;
import com.ndashimye.firstapp.todo.TodoDTO;
import com.ndashimye.firstapp.todo.TodoService;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class TaskGenerationService {
    private final TodoService todoService;
    private final TaskService taskService;
    private final GeneratedTaskDTOMapper generatedTaskDTOMapper;


    public List<GeneratedTaskDTO> generateTasksOfTodo(Long todoId, Integer numberOfTasks)
            throws AppEntityNotFoundException {

        numberOfTasks = numberOfTasks > 10 ? 10 : numberOfTasks;

        TodoDTO todo = todoService.getTodoDTOById(todoId);
        List<TaskDTO> tasks = taskService.getLastTasksByTodoId(todoId);

        //Get the list of the last three tasks of the todoItem ranked by position
        List<GeneratedTaskDTO> lastTodoTasks = tasks.subList(
                        0,
                        Math.min(tasks.size(), 3))
                .stream().map(generatedTaskDTOMapper).collect(Collectors.toList());


        Gson gson = new Gson();
        String json = gson.toJson(lastTodoTasks);

        String prompt = "Here is a todo of ID: " + todo.id() + "" +
                ", name: '" + todo.name() + "'" +
                " and description: '" + todo.description() + "'. " +
                "Generate " + numberOfTasks + "" +
                " tasks or sub-tasks that will need to be executed in order to complete the todo," +
                ", they should be helpful, non-repetitive, straightforward, clear, concise and easy to understand," +
                " in JSON format with the following properties: " +
                "todoId(which is equal to the todo ID)" +
                ", name(regexp = ^[a-zA-Z]([a-zA-Z0-9]|[-_. ](?![._-])){1,48}[a-zA-Z0-9]$ )" +
                " and priorityLevel(which is an integer between 1 and 5)." +
                " The generated tasks should be a continuity of the following tasks: " + json + ".";


        System.out.println(prompt);
        return generateTasks(prompt);
    }


    public List<GeneratedTaskDTO> generateChildTasksOfTask(Long taskId, Integer numberOfChildTasks)
            throws AppEntityNotFoundException {

        numberOfChildTasks = numberOfChildTasks > 5 ? 5 : numberOfChildTasks;
        TaskDTO task = taskService.getTaskDTOById(taskId);
        TodoDTO todo = todoService.getTodoDTOById(task.todoId());

        List<TaskDTO> childTasks = taskService.getLastChildTasksByTaskId(taskId);

        //Get the list of the last three child tasks of the task ranked by position
        List<GeneratedTaskDTO> lastChildTasks = childTasks.subList(
                        0,
                        Math.min(childTasks.size(), 3))
                .stream().map(generatedTaskDTOMapper).collect(Collectors.toList());


        Gson gson = new Gson();
        String json = gson.toJson(lastChildTasks);

        String prompt = "Here is a task of ID: '" + task.id() + "'" +
                ", name: '" + task.name() + "'" +
                "The task belongs to a todo of ID: " + todo.id() + ". " +
                "Understand the specific task and Generate " + numberOfChildTasks + "" +
                " child tasks or sub-tasks that will need to be executed in order to complete the task" +
                ", they should be helpful, non-repetitive, straightforward, clear, concise and easy to understand," +
                " in JSON format with the following properties: " +
                "todoId(which is equal to the todo ID)" +
                ", name(regexp = ^[a-zA-Z]([a-zA-Z0-9]|[-_. ](?![._-])){1,48}[a-zA-Z0-9]$ )" +
                " and priorityLevel(which is an integer between 1 and 5)." +
                " The generated child tasks or sub-tasks should be a continuity of the following tasks: " + json + ".";

        System.out.println(prompt);
        return generateTasks(prompt);
    }


    private List<GeneratedTaskDTO> generateTasks(String prompt) {
        List<ChatMessage> chatMessages = new ArrayList();
        ChatMessage message = new ChatMessage("user", prompt);
        chatMessages.add(message);

        OpenAiService service = new OpenAiService("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", Duration.ofMinutes(1));
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(chatMessages)
                .temperature(0.3)
                .build();

        //Get the first choice and its content
        String response = service.createChatCompletion(completionRequest).getChoices().get(0)
                .getMessage().getContent();

        //Convert the response into a list of generated tasks
        ObjectMapper mapper = new ObjectMapper();
        List<GeneratedTaskDTO> generatedTasks = null;
        try {
            generatedTasks = mapper.readValue(response
                    , new TypeReference<List<GeneratedTaskDTO>>() {
                    });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return generatedTasks;
    }
}
