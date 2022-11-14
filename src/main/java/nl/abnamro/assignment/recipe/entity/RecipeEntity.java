package nl.abnamro.assignment.recipe.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "recipe")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer portions;

    @Column(name = "is_vegetarian")
    private Boolean isVegetarian;

    private String instructions;

    private String ingredients;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private UserEntity user;

}
