package learning.assignment.dto;

import jakarta.validation.constraints.NotBlank;

public class UserAuthDTO {
    @NotBlank
    public String username;

    @NotBlank
    public String password;
}
