package learning.assignment.service.task;

import learning.assignment.dto.TaskDTO;
import learning.assignment.exceptions.ResourceNotFoundException;
import learning.assignment.model.Task;
import learning.assignment.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {
    
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    public Task createTask(TaskDTO taskDTO) {
        Task task = new Task();
        setFields(taskDTO, task);
        return taskRepository.save(task);
    }

    public Task updateTask(Long taskId, TaskDTO taskDTO) {
        Task task = getTask(taskId);
        setFields(taskDTO, task);
        return taskRepository.save(task);
    }

    public boolean deleteTask(Long taskId) {
        if (taskRepository.existsById(taskId)) {
            taskRepository.deleteById(taskId);
            return true;
        } else throw new ResourceNotFoundException("Task not found");
    }

    public Task getTask(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    private static void setFields(TaskDTO taskDTO, Task task) {
        task.setTitle(taskDTO.title);
        task.setStatus(taskDTO.status);
        task.setDueDate(taskDTO.dueDate);
    }
}
