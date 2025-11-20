package learning.assignment.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name="Tasks")
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String projectId;
    private String title;
    private String status;
    private Date dueDate;
}
