package nl.abnamro.assignment.recipe.entity;

import lombok.Data;

import javax.persistence.*;


@Entity
@Data
@Table(name = "user")
public class UserEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String userName;

    private String password;



}
