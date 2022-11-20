package nl.abnamro.assignment.recipe.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AppUtils {

    private static final String WILDCARD = "#/#";
    private static final String COMMA = ",";

    public static String getIngredientsAsString(Set<String> ingredientsSet) {
        String ingredients = null;
        if (ingredientsSet != null) {
            ingredients = String.join(",", ingredientsSet.stream().map(s -> s.replace(COMMA, WILDCARD)).toList());
        }
        return ingredients;
    }

    public static Set<String> getIngredientsAsSet(String ingredients) {
        if (ingredients != null) {
            List<String> ingredientsList = Arrays.asList(ingredients.split(","));
            return new HashSet<>(ingredientsList.stream().map(i -> i.replaceAll(WILDCARD, COMMA)).toList());
        }
        return Set.of();
    }

}
