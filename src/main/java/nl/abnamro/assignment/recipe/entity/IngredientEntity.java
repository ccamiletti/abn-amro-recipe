package nl.abnamro.assignment.recipe.entity;

import java.util.Set;

//@Entity
//@Data
//@Table(name = "ingredient")
public class IngredientEntity {

    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    //@ManyToMany(mappedBy = "ingredients")
    private Set<RecipeEntity> recipesList;

}
