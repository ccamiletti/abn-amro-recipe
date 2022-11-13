package nl.abnamro.assignment.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class RecipeDTO {

    private Long id;

    private String name;

    private Integer portions;

    private Boolean isVegetarian;

    private String instructions;

    private Set<String> ingredients;

}
