package nl.abnamro.assignment.recipe.controller;


import lombok.AllArgsConstructor;
import nl.abnamro.assignment.recipe.dto.RecipeDTO;
import nl.abnamro.assignment.recipe.exception.RecipeException;
import nl.abnamro.assignment.recipe.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody RecipeDTO recipeDTO) throws RecipeException {
        recipeService.add(recipeDTO);
        return ResponseEntity.accepted().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateRecipe(@PathVariable(name = "id") Long id, @RequestBody RecipeDTO recipeDTO) throws RecipeException {
        recipeService.update(id, recipeDTO);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeById(@PathVariable(name = "id") Long id) {
        recipeService.delete(id);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDTO> findById(@PathVariable(name = "id") Long id) throws Exception {
        return new ResponseEntity<RecipeDTO>(recipeService.findById(id), HttpStatus.OK);
    }

    @GetMapping
    public List<RecipeDTO> getAll() {
        return recipeService.findAllByUser();
    }

    @GetMapping("/filter")
    public List<RecipeDTO> getRecipesFiltered(@RequestParam(value = "isVegetarian", required = false) Boolean isVegetarian,
                                   @RequestParam(value = "portions", required = false) Integer portions,
                                   @RequestParam(value = "includeIngredients", required = false) Set<String> includeIngredients,
                                   @RequestParam(value = "excludeIngredients", required = false) Set<String> excludeIngredients,
                                   @RequestParam(value = "includeInstructions", required = false) String includeInstructions) {


        return recipeService.findByCriteria(isVegetarian, portions, includeIngredients, excludeIngredients, includeInstructions);

    }


}
