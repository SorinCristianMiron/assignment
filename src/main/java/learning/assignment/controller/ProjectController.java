package learning.assignment.controller;

import jakarta.validation.Valid;
import learning.assignment.dto.ProjectDTO;
import learning.assignment.model.Project;
import learning.assignment.service.project.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/createProject")
    public ResponseEntity<Project> createProject(@Valid @RequestBody ProjectDTO projectDTO) {
        return new ResponseEntity<>(projectService.createProject(projectDTO), HttpStatus.CREATED);
    }

    @PatchMapping("/updateProject")
    public ResponseEntity<Project> updateProject(@RequestParam Long projectId, @Valid @RequestBody ProjectDTO projectDTO) {
        return ResponseEntity.ok(projectService.updateProject(projectId, projectDTO));
    }

    @DeleteMapping("/deleteProject")
    public ResponseEntity<Boolean> deleteProject(@RequestParam Long projectId) {
        return ResponseEntity.ok(projectService.deleteProject(projectId));
    }

    @GetMapping("/getProject")
    public ResponseEntity<Project> getProject(@RequestParam Long projectId) {
        return ResponseEntity.ok(projectService.getProject(projectId));
    }
}
