package learning.assignment.dto;

import learning.assignment.util.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchDTO {
    public String username;
    public String email;
    public Set<Roles> roles;
}
