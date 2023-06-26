package com.ndashimye.firstapp.goal;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.task.TaskCreationDTO;
import com.ndashimye.firstapp.task.TaskDTO;
import com.ndashimye.firstapp.task.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/goals")
@AllArgsConstructor
public class GoalController {

    private final GoalService goalService;
    private final TaskService taskService;



    //HTTP endpoints that handle all the operations related to goals

    @PutMapping("/{goalId}")
    public void updateGoal(@PathVariable Long goalId, @RequestBody GoalCreationDTO updatedGoal)
            throws AppEntityNotFoundException {

        goalService.updateGoal(updatedGoal, goalId);
    }

    @PutMapping("/{goalId}/update/position")
    public void updateGoalPosition(@PathVariable Long goalId,
                                   @RequestParam Integer position)
            throws AppEntityNotFoundException {

        goalService.updateGoalPositionInProject(goalId, position);
    }

    @DeleteMapping("/{goalId}")
    public void deleteGoal(@PathVariable Long goalId)
            throws AppEntityNotFoundException {

        goalService.deleteGoal(goalId);
    }

    @GetMapping("/{goalId}")
    public GoalDTO getGoalById(@PathVariable Long goalId)
            throws AppEntityNotFoundException {

        return goalService.getGoalDTOById(goalId);
    }



    /*

    HTTP endpoints that handle all the operations
    related to the relationship between goals and projects

    */

    @PutMapping("/{goalId}/move-to/{projectId}")
    public void moveToProject(@PathVariable Long goalId,
                              @PathVariable Long projectId)
            throws AppEntityNotFoundException {

        goalService.moveGoalToProject(goalId, projectId);
    }



    /*

    HTTP endpoints that handle all the operations
    related to the relationship between goals and tasks

    */

    @PostMapping("/{goalId}/tasks")
    public void addTaskToGoal(@RequestBody TaskCreationDTO task, @PathVariable Long goalId)
            throws AppEntityNotFoundException {

        taskService.addNewTaskToGoal(task, goalId);
    }

    @GetMapping("/{goalId}/tasks")
    public List<TaskDTO> getTasksByGoalId(@PathVariable Long goalId)
            throws AppEntityNotFoundException {

        return taskService.getAllTasksByGoalId(goalId);
    }

    @GetMapping("/{goalId}/tasks/order-by/priority")
    public List<TaskDTO> getTasksByGoalIdOrderedByPriority
            (@PathVariable Long goalId)
            throws AppEntityNotFoundException {

        return taskService.getAllTasksByGoalIdOrderedByPriority(goalId);
    }

    @GetMapping("/{goalId}/tasks/completed")
    public List<TaskDTO> getCompletedTasks(@PathVariable Long goalId)
            throws AppEntityNotFoundException {

        return taskService.getCompletedTasks(goalId);
    }

    @GetMapping("/{goalId}/tasks/uncompleted")
    public List<TaskDTO> getUncompletedTasks(@PathVariable Long goalId)
            throws AppEntityNotFoundException {

        return taskService.getUncompletedTasks(goalId);
    }
}
