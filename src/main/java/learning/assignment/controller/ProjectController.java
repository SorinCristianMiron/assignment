package learning.assignment.controller;

import learning.assignment.dto.ProjectDTO;
import learning.assignment.model.Project;
import learning.assignment.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/createProject")
    public ResponseEntity<String> createProject(@RequestBody ProjectDTO projectDTO) {
        String response = projectService.createProject(projectDTO);
        if (response.equals("success")) return new ResponseEntity<>(response, HttpStatus.CREATED);
        else return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @PatchMapping("/updateProject")
    public ResponseEntity<String> updateProject(@RequestParam Long projectId, @RequestBody ProjectDTO projectDTO) {
        String response = projectService.updateProject(projectId, projectDTO);
        if (response.equals("success")) return new ResponseEntity<>(response, HttpStatus.OK);
        else if (response.equals("not_found")) return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("/deleteProject")
    public ResponseEntity<String> deleteProject(@RequestParam Long projectId) {
        String response = projectService.deleteProject(projectId);
        if (response.equals("success")) return new ResponseEntity<>(response, HttpStatus.OK);
        else if (response.equals("not_found")) return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/getProject")
    public ResponseEntity<Object> getProject(@RequestParam Long projectId) {
        Object response = projectService.getProject(projectId);
        if (response instanceof Project) return new ResponseEntity<>(response, HttpStatus.OK);
        if (response.equals("not_found")) return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}
