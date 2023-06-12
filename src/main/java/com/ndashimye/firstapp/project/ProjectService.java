package com.ndashimye.firstapp.project;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import java.util.List;

public interface ProjectService {

    Project getProjectById(Long projectId) throws AppEntityNotFoundException;
}
