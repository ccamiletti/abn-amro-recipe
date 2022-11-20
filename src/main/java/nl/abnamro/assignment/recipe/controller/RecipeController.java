package nl.abnamro.assignment.recipe.controller;


import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;
import nl.abnamro.assignment.recipe.dto.RecipeDTO;
import nl.abnamro.assignment.recipe.exception.RecipeException;
import nl.abnamro.assignment.recipe.service.RecipeService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecipeDTO> findById(@PathVariable(name = "id") Long id) throws Exception {
        return new ResponseEntity<RecipeDTO>(recipeService.findById(id), HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (1..N)", defaultValue = "1"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page.", defaultValue = "5")
    })
    public List<RecipeDTO> getAll(@PageableDefault(size = 10) Pageable pageable) {
        return recipeService.findAllByUser(pageable);
    }

    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (1..N)", defaultValue = "1"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page.", defaultValue = "5")
    })
    public List<RecipeDTO> getRecipesFiltered(@RequestParam(value = "isVegetarian", required = false) Boolean isVegetarian,
                                   @RequestParam(value = "portions", required = false) Integer portions,
                                   @RequestParam(value = "includeIngredients", required = false) Set<String> includeIngredients,
                                   @RequestParam(value = "excludeIngredients", required = false) Set<String> excludeIngredients,
                                   @RequestParam(value = "includeInstructions", required = false) String includeInstructions,
                                              @PageableDefault(page = 1, size = 5) Pageable pageable) {

        return recipeService.findByCriteria(isVegetarian, portions, includeIngredients, excludeIngredients, includeInstructions, pageable);

    }


}
