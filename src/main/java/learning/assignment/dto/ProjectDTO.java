package learning.assignment.dto;

import jakarta.validation.constraints.NotBlank;
import learning.assignment.util.Roles;

import java.util.ArrayList;
import java.util.Date;

public class ProjectDTO {
    @NotBlank
    public String name;
    public String description;
//    public Date createdAt;
//    public String ownerId;
}
