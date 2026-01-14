package learning.assignment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public class TaskDTO {
    @NotBlank
    public String title;

    @NotBlank
    public String status;

    @FutureOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date dueDate;
}
