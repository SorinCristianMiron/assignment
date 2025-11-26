package learning.assignment.dto;

import jakarta.validation.constraints.NotNull;

public class UserAuthDTO {
    @NotNull
    public String username;
    @NotNull
    public String password;
}
