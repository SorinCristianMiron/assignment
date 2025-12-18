package learning.assignment.service.project;

import learning.assignment.dto.ProjectDTO;
import learning.assignment.exceptions.ResourceNotFoundException;
import learning.assignment.model.Project;
import learning.assignment.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Transactional
    public Project createProject(ProjectDTO projectDTO) {
        Project project = new Project();
        setFields(projectDTO, project);
        project.setCreatedAt(Date.valueOf(LocalDate.now()));
        return projectRepository.save(project);
    }

    public Project updateProject(Long projectId, ProjectDTO projectDTO) {
        Project project = getProject(projectId);
        setFields(projectDTO, project);
        return projectRepository.save(project);
    }

    public boolean deleteProject(Long projectId) {
        if (projectRepository.existsById(projectId)) {
            projectRepository.deleteById(projectId);
            return true;
        } else throw new ResourceNotFoundException("Project not found");
    }

    public Project getProject(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Project not found"));
    }

    private static void setFields(ProjectDTO projectDTO, Project project) {
        project.setName(projectDTO.name);
        project.setDescription(projectDTO.description);
    }
}
