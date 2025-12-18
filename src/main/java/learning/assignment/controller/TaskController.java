package learning.assignment.controller;

import learning.assignment.dto.TaskDTO;
import learning.assignment.model.Task;
import learning.assignment.service.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class TaskController {
    
    private final TaskService taskService;
    
    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/createTask")
    public ResponseEntity<Task> createTask(@RequestBody TaskDTO taskDTO) {
        return new ResponseEntity<>(taskService.createTask(taskDTO), HttpStatus.CREATED);
    }

    @PatchMapping("/updateTask")
    public ResponseEntity<Task> updateTask(@RequestParam Long taskId, @RequestBody TaskDTO taskDTO) {
        return ResponseEntity.ok(taskService.updateTask(taskId, taskDTO));
    }

    @DeleteMapping("/deleteTask")
    public ResponseEntity<Boolean> deleteTask(@RequestParam Long taskId) {
        return ResponseEntity.ok(taskService.deleteTask(taskId));
    }

    @GetMapping("/getTask")
    public ResponseEntity<Task> getTask(@RequestParam Long taskId) {
        return ResponseEntity.ok(taskService.getTask(taskId));
    }
}
