package nl.abnamro.assignment.recipe.service;

import lombok.AllArgsConstructor;
import nl.abnamro.assignment.recipe.dto.RecipeDTO;
import nl.abnamro.assignment.recipe.dto.UserDTO;
import nl.abnamro.assignment.recipe.entity.RecipeEntity;
import nl.abnamro.assignment.recipe.entity.UserEntity;
import nl.abnamro.assignment.recipe.repository.RecipeRepository;
import nl.abnamro.assignment.recipe.repository.UserRepository;
import nl.abnamro.assignment.recipe.util.AppUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Optional;
import java.util.Set;

import static nl.abnamro.assignment.recipe.util.AppUtils.getIngredientsAsSet;

@Service
@AllArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public RecipeDTO findById(Long id) throws Exception {
        return recipeRepository.findByIdAndUserId(id,  getUserId())
                .map(this::toDTO).orElseThrow(() -> new Exception("recipe not found"));
    }

    public RecipeDTO findByName(String name) throws Exception {
        return recipeRepository.findByNameAndUserId(name, getUserId())
                .map(this::toDTO).orElseThrow(() -> new Exception("recipe not found"));
    }

    public String add(RecipeDTO recipeDTO) {
        recipeRepository.findByNameAndUserId(recipeDTO.getName(), getUserId())
                .ifPresentOrElse((v) -> {
                    throw new RuntimeException("BAD REQUEST, recipe already exist");
                }, () -> {
                    recipeRepository.save(toEntity(recipeDTO));
                });
        return "recipe was added";
    }

    public String delete(Long id) {
        //recipeRepository.deleteByIdAndUserId(id, getUserId());
        return "recipe was removed !!!";
    }

    public String update(Long id, RecipeDTO recipeDTO) {
        recipeRepository.findByIdAndUserId(id, getUserId())
                .ifPresentOrElse((recipeEntity) -> {
                    updateRecipeEntity(recipeDTO, recipeEntity);
                    recipeRepository.save(recipeEntity);
                }, () -> {
                    throw new RuntimeException("BAD REQUEST, recipe not found");
                });
        return "recipe was updated !!!";
    }

    public Flux<RecipeDTO> findAllByUser()  {
        return recipeRepository.findByUserId(getUserId()).map(this::toDTO);
    }

//    public List<RecipeDTO> findVegetarian()  {
//        return recipeRepository.findByUserId(getUserId())
//                .stream()
//                .filter(recipeEntity -> recipeEntity.getIsVegetarian())
//                .map(this::toDTO)
//                .toList();
//    }
//
//    public List<RecipeDTO> findByCriteria(Boolean isVegetarian, Integer portions, Set<String> includeIngredients,
//                                                Set<String> excludeIngredients, String instructionText) {
//
//        return recipeRepository.findByUserId(getUserId())
//                .stream()
//                .filter(recipeEntity -> filterSearch(isVegetarian, recipeEntity.getIsVegetarian()))
//                .filter(recipeEntity -> filterSearch(portions, recipeEntity.getPortions()))
//                .filter(recipeEntity -> filterIngredients(includeIngredients, recipeEntity.getIngredients(), Boolean.TRUE))
//                .filter(recipeEntity -> filterIngredients(excludeIngredients, recipeEntity.getIngredients(), Boolean.FALSE))
//                .filter(recipeEntity -> filterInstructionText(instructionText, recipeEntity.getInstructions()))
//                .map(this::toDTO)
//                .toList();
//
//    }

    private RecipeEntity toEntity(RecipeDTO recipeDTO) {
        return RecipeEntity.builder()
                .name(recipeDTO.getName())
                .instructions(recipeDTO.getInstructions())
                .ingredients(AppUtils.getIngredientsAsString(recipeDTO.getIngredients()))
                .isVegetarian(recipeDTO.getIsVegetarian())
                .portions(recipeDTO.getPortions())
                .user(getUserEntity())
                .build();
    }

    private RecipeDTO toDTO(RecipeEntity recipeEntity) {
        return RecipeDTO.builder()
                .id(recipeEntity.getId())
                .name(recipeEntity.getName())
                .instructions(recipeEntity.getInstructions())
                .ingredients(getIngredientsAsSet(recipeEntity.getIngredients()))
                .isVegetarian(recipeEntity.getIsVegetarian())
                .portions(recipeEntity.getPortions()).build();
    }

    private UserEntity getUserEntity() {
        try {
            return userRepository.findById(getUserId()).orElseThrow(() -> new RuntimeException("user not found"));
        } catch (Exception e) {
            System.out.println("error");
            throw e;
        }
    }

    private Long getUserId() {
        return ((UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }

    private void updateRecipeEntity(RecipeDTO recipeDTO, RecipeEntity recipeEntity) {
        recipeEntity.setIngredients(recipeDTO.getIngredients() != null ? AppUtils.getIngredientsAsString(recipeDTO.getIngredients()) :
                recipeEntity.getIngredients());
        recipeEntity.setName(recipeDTO.getName() != null ? recipeDTO.getName() : recipeEntity.getName());
        recipeEntity.setPortions(recipeDTO.getPortions() != null ? recipeDTO.getPortions() : recipeEntity.getPortions());
        recipeEntity.setInstructions(recipeDTO.getInstructions() != null ? recipeDTO.getInstructions() : recipeEntity.getInstructions());
        recipeEntity.setIsVegetarian(recipeDTO.getIsVegetarian() != null ? recipeDTO.getIsVegetarian() : recipeEntity.getIsVegetarian());
    }


    private Boolean filterIngredients(Set<String> filterIngredients, String ingredients, Boolean isInclude) {
        if (filterIngredients != null) {
            Set<String> ingredientsSet = AppUtils.getIngredientsAsSet(ingredients);
            return (isInclude) ? filterIngredients.stream().allMatch(s -> ingredientsSet.contains(s)) :
                    filterIngredients.stream().noneMatch(s -> ingredientsSet.contains(s));
        }
        return Boolean.TRUE;

    }

    private boolean filterInstructionText(String filterInstructionText, String instructions) {
        return Optional.ofNullable(filterInstructionText)
                .map(fp -> instructions.contains(filterInstructionText))
                .orElse(Boolean.TRUE);
    }

    private boolean filterSearch(Object filter, Object value) {
        return Optional.ofNullable(filter)
                .map(fp -> filter.equals(value))
                .orElse(Boolean.TRUE);
    }

}
