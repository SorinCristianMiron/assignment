package learning.assignment.model;

import jakarta.persistence.*;
import learning.assignment.util.Roles;
import lombok.Data;

import java.util.ArrayList;

@Entity
@Table(name="Users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String passwordHash;
    private String email;
    private ArrayList<Roles> roles;
    private String description;

}
