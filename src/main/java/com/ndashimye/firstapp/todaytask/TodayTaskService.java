package com.ndashimye.firstapp.todaytask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Objects;
import java.util.Optional;

@Service
public class TodayTaskService {

    @Autowired
    TodayTaskRepository todayTaskRepository;

    public TodayTask getTodayTaskById(Integer todayTaskId) throws TodayTaskNotFoundException {

        Optional<TodayTask> todayTask = todayTaskRepository.findById(todayTaskId);

        if(!todayTask.isPresent()){
            throw new TodayTaskNotFoundException();
        }
        return todayTask.get();

    }

    public void addNewTodayTask(TodayTask todayTask) {
        //set userTodo order
        todayTaskRepository.save(todayTask);
    }

    public void updateTodayTask(TodayTask updatedTodayTask, TodayTask todayTask) {

        if (Objects.nonNull(updatedTodayTask.getTask()) && !updatedTodayTask.getTask().equals("")) {
            todayTask.setTask(updatedTodayTask.getTask());
        }
        if (Objects.nonNull(updatedTodayTask.getDate()) && !updatedTodayTask.getDate().equals("")) {
            todayTask.setDate(updatedTodayTask.getDate());
        }
        todayTaskRepository.save(todayTask);
    }

    public void deleteTodayTask(TodayTask todayTask) {
        todayTaskRepository.delete(todayTask);
    }
}
