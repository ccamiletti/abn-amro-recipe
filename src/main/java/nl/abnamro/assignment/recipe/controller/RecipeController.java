package nl.abnamro.assignment.recipe.controller;


import lombok.AllArgsConstructor;
import nl.abnamro.assignment.recipe.dto.RecipeDTO;
import nl.abnamro.assignment.recipe.service.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@AllArgsConstructor
@RequestMapping("/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody RecipeDTO recipeDTO) {
        String res = null;
        try {
            res = recipeService.add(recipeDTO);
        }catch(Exception e) {
            System.out.println("error: " + e.getMessage());
        }
        return ResponseEntity.ok("recipe was added");
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<String> updateRecipe(@PathVariable(name = "id") Long id, @RequestBody RecipeDTO recipeDTO) {
        recipeService.update(id, recipeDTO);
        return ResponseEntity.ok("the recipe was updated !!");
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeById(@PathVariable(name = "id") Long id) {
        recipeService.delete(id);
        return ResponseEntity.ok("recipe was deleted");
    }

    @GetMapping("/findById/{id}")
    public RecipeDTO findById(@PathVariable(name = "id") Long id) throws Exception {
        return recipeService.findById(id);
    }

    @GetMapping("/findByName/{name}")
    public RecipeDTO findByName(@PathVariable(name = "name") String name) throws Exception {
        return recipeService.findByName(name);
    }

    @GetMapping("/findAll")
    public Flux<RecipeDTO> getAll() {
        return recipeService.findAllByUser();
    }

//    @GetMapping("/getVegetarian")
//    public List<RecipeDTO> getVegetarian() {
//        return recipeService.findVegetarian();
//    }

//    @GetMapping("/filter")
//    public List<RecipeDTO> getRecipesFiltered(@RequestParam(value = "isVegetarian", required = false) Boolean isVegetarian,
//                                   @RequestParam(value = "portions", required = false) Integer portions,
//                                   @RequestParam(value = "includeIngredients", required = false) Set<String> includeIngredients,
//                                   @RequestParam(value = "excludeIngredients", required = false) Set<String> excludeIngredients,
//                                   @RequestParam(value = "instructionText", required = false) String instructionText) {
//
//
//        return recipeService.findByCriteria(isVegetarian, portions, includeIngredients, excludeIngredients, instructionText);
//
//    }


}
