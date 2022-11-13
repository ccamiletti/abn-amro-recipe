package nl.abnamro.assignment.recipe.entity;

import java.io.Serializable;

//@Embeddable
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
public class RecipeIngredientId implements Serializable {

    private static final long serialVersionUID = 5143529774382452054L;

    //@Column(name = "recipe_id")
    private Long recipeId;

    //@Column(name = "ingredient_id")
    private Long ingredientId;

}
