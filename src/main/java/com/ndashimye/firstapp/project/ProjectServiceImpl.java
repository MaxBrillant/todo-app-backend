package com.ndashimye.firstapp.project;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.user.User;
import com.ndashimye.firstapp.user.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private ProjectRepository projectRepository;



    @Override
    public Project getProjectById(Long projectId) throws AppEntityNotFoundException {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new AppEntityNotFoundException(Project.class));
    }
}
