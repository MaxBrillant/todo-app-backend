package com.ndashimye.firstapp.goalGeneration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.goal.GoalDTO;
import com.ndashimye.firstapp.goal.GoalService;
import com.ndashimye.firstapp.userproject.ProjectRole;
import com.ndashimye.firstapp.userproject.UserProject;
import com.ndashimye.firstapp.userproject.UserProjectService;
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
public class GoalGenerationService {
    private final String API_KEY = System.getenv("OPENAI_API_KEY");
    private final UserProjectService userProjectService;
    private final GoalService goalService;
    private final GeneratedGoalDTOMapper generatedGoalDTOMapper;

    public List<GeneratedGoalDTO> generateGoalsOfProject(Long userId, Long projectId, Integer numberOfGoals)
            throws AppEntityNotFoundException, IllegalAccessException {

        numberOfGoals = numberOfGoals > 10 ? 10 : numberOfGoals;

        UserProject userProject = userProjectService.getUserProjectByUserIdAndProjectId(userId, projectId);

        if (userProject.getProjectRole().equals(ProjectRole.CREATOR) ||
                userProject.getProjectRole().equals(ProjectRole.ADMIN)) {

            List<GoalDTO> goals = goalService.getLastGoalsOfUserByProjectId(userId, projectId);

            //Get the list of the last three goals of the project ranked by position and without their descriptions
            List lastProjectGoals = goals.subList(
                            0,
                            Math.min(goals.size(), 3))
                    .stream().map(generatedGoalDTOMapper)
                    .map(generatedGoalDTO -> new GeneratedGoalDTO(generatedGoalDTO.projectId(),
                            generatedGoalDTO.name(),
                            null
                    )).collect(Collectors.toList());


            Gson gson = new Gson();
            String json = gson.toJson(lastProjectGoals);

            String prompt = "Here is a project of ID: " + userProject.getProject().getProjectId() + "" +
                    ", name: '" + userProject.getProject().getName() + "'" +
                    " and description: '" + userProject.getProject().getDescription() + "'. " +
                    "Generate " + numberOfGoals + "" +
                    " goals or actions that will need to be executed in order to complete the project," +
                    " they should be helpful, non-repetitive, straightforward, clear, concise and easy to understand," +
                    " in JSON format with the following properties: " +
                    "projectId(which is equal to the project ID)" +
                    ", name(regexp = ^[a-zA-Z]([a-zA-Z0-9]|[-_. ](?![._-])){1,48}[a-zA-Z0-9]$ )" +
                    " and description(should be unique, creative, well-written, descriptive and easy to understand, " +
                    "regexp = ^[\\w\\s.,;:!?'\\\"(){}\\[\\]-_*&#@^+=|%$\\/]{10,500}$ )." +
                    " The generated goals or actions should be a continuity of the following goals: " + json + ".";

            System.out.println(prompt);
            return generateGoals(prompt);
        } else {
            throw new IllegalAccessException
                    ("You do not have the required permissions to generate goals in this project");
        }
    }


    private List<GeneratedGoalDTO> generateGoals(String prompt) {
        List<ChatMessage> chatMessages = new ArrayList();
        ChatMessage message = new ChatMessage("user", prompt);
        chatMessages.add(message);

        OpenAiService service = new OpenAiService(API_KEY, Duration.ofMinutes(1));
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
        List<GeneratedGoalDTO> generatedGoals = null;
        try {
            generatedGoals = mapper.readValue(response
                    , new TypeReference<List<GeneratedGoalDTO>>() {
                    });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return generatedGoals;
    }
}
