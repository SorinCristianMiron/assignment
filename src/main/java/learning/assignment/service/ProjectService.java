package learning.assignment.service;

import learning.assignment.dto.ProjectDTO;
import learning.assignment.model.Project;
import learning.assignment.model.User;
import learning.assignment.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserService userService;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, UserService userService) {
        this.projectRepository = projectRepository;
        this.userService = userService;
    }

    @Transactional
    public String createProject(ProjectDTO projectDTO) {
        Optional<User> currentUser = userService.getCurrentUser();
        if(currentUser.isEmpty()) return "not_logged_in";

        Project project = new Project();
        setFields(projectDTO, project);
        project.setCreatedAt(Date.valueOf(LocalDate.now()));
        project.setOwnerId(currentUser.get().getId());
        projectRepository.save(project);
        return "success";
    }

    public String updateProject(Long projectId, ProjectDTO projectDTO) {
        Optional<User> currentUser = userService.getCurrentUser();
        if(currentUser.isEmpty()) return "not_logged_in";

        Optional<Project> project = projectRepository.findById(projectId);
        if(project.isEmpty()) return "not_found";

        setFields(projectDTO, project.get());

        projectRepository.save(project.get());
        return "success";
    }

    public String deleteProject(Long projectId) {
        Optional<User> currentUser = userService.getCurrentUser();
        if(currentUser.isEmpty()) return "not_logged_in";

        Optional<Project> project = projectRepository.findById(projectId);
        if(project.isEmpty()) return "not_found";

        projectRepository.delete(project.get());
        return "success";
    }

    public Object getProject(Long projectId) {
        Optional<User> currentUser = userService.getCurrentUser();
        if(currentUser.isEmpty()) return "not_logged_in";

        Optional<Project> project = projectRepository.findById(projectId);
        if (project.isEmpty()) return "not_found";

        else return project.get();
    }

    private static void setFields(ProjectDTO projectDTO, Project project) {
        project.setName(projectDTO.name);
        project.setDescription(projectDTO.description);
    }
}
