package nl.abnamro.assignment.recipe.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;


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

    @OneToMany(mappedBy = "user")
    private Set<RecipeEntity> recipeEntitySet;



}
