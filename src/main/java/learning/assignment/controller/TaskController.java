package learning.assignment.controller;

import learning.assignment.dto.TaskDTO;
import learning.assignment.model.Task;
import learning.assignment.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TaskController {
    
    private final TaskService taskService;
    
    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/createTask")
    public ResponseEntity<String> createTask(@RequestParam Long projectId, @RequestBody TaskDTO taskDTO) {
        String response = taskService.createTask(projectId, taskDTO);
        if (response.equals("success")) return new ResponseEntity<>(response, HttpStatus.CREATED);
        else if (response.equals("not_found")) return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @PatchMapping("/updateTask")
    public ResponseEntity<String> updateTask(@RequestParam Long taskId, @RequestBody TaskDTO taskDTO) {
        String response = taskService.updateTask(taskId, taskDTO);
        if (response.equals("success")) return new ResponseEntity<>(response, HttpStatus.OK);
        else if (response.equals("not_found")) return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("/deleteTask")
    public ResponseEntity<String> deleteTask(@RequestParam Long taskId) {
        String response = taskService.deleteTask(taskId);
        if (response.equals("success")) return new ResponseEntity<>(response, HttpStatus.OK);
        else if (response.equals("not_found")) return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/getTask")
    public ResponseEntity<Object> getTask(@RequestParam Long taskId) {
        Object response = taskService.getTask(taskId);
        if (response instanceof Task) return new ResponseEntity<>(response, HttpStatus.OK);
        if (response.equals("not_found")) return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}
