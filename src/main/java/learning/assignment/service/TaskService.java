package learning.assignment.service;

import learning.assignment.dto.TaskDTO;
import learning.assignment.model.Project;
import learning.assignment.model.Task;
import learning.assignment.model.User;
import learning.assignment.repository.ProjectRepository;
import learning.assignment.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TaskService {
    
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final ProjectRepository projectRepository;
    
    @Autowired
    public TaskService(TaskRepository taskRepository, UserService userService, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.projectRepository = projectRepository;
    }

    @Transactional
    public String createTask(Long projectId, TaskDTO taskDTO) {
        Optional<User> currentUser = userService.getCurrentUser();
        if(currentUser.isEmpty()) return "not_logged_in";

        Optional<Project> project = projectRepository.findById(projectId);
        if(project.isEmpty()) return "not_found";

        Task task = new Task();
        setFields(taskDTO, task);
        task.setProjectId(projectId);
        taskRepository.save(task);
        return "success";
    }

    public String updateTask(Long taskId, TaskDTO taskDTO) {
        Optional<User> currentUser = userService.getCurrentUser();
        if(currentUser.isEmpty()) return "not_logged_in";

        Optional<Task> task = taskRepository.findById(taskId);
        if(task.isEmpty()) return "not_found";

        setFields(taskDTO, task.get());

        taskRepository.save(task.get());
        return "success";
    }

    public String deleteTask(Long taskId) {
        Optional<User> currentUser = userService.getCurrentUser();
        if(currentUser.isEmpty()) return "not_logged_in";

        Optional<Task> task = taskRepository.findById(taskId);
        if(task.isEmpty()) return "not_found";

        taskRepository.delete(task.get());
        return "success";
    }

    public Object getTask(Long taskId) {
        Optional<User> currentUser = userService.getCurrentUser();
        if(currentUser.isEmpty()) return "not_logged_in";

        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isEmpty()) return "not_found";

        else return task.get();
    }

    private static void setFields(TaskDTO taskDTO, Task task) {
        task.setTitle(taskDTO.title);
        task.setStatus(taskDTO.status);
        task.setDueDate(taskDTO.dueDate);
    }
}
