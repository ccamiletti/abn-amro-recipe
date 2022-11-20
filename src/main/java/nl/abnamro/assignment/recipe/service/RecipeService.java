package nl.abnamro.assignment.recipe.service;

import lombok.AllArgsConstructor;
import nl.abnamro.assignment.recipe.dto.RecipeDTO;
import nl.abnamro.assignment.recipe.dto.UserDTO;
import nl.abnamro.assignment.recipe.entity.RecipeEntity;
import nl.abnamro.assignment.recipe.entity.UserEntity;
import nl.abnamro.assignment.recipe.exception.RecipeException;
import nl.abnamro.assignment.recipe.repository.RecipeRepository;
import nl.abnamro.assignment.recipe.repository.UserRepository;
import nl.abnamro.assignment.recipe.util.AppUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static nl.abnamro.assignment.recipe.util.AppUtils.getIngredientsAsSet;

@Service
@AllArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public RecipeDTO findById(Long id) throws RecipeException {
        return recipeRepository.findByIdAndUserId(id,  getUserId())
                .map(this::toDTO).orElseThrow(() -> new RecipeException("Error getting recipe by id", HttpStatus.BAD_REQUEST));
    }

    public void add(RecipeDTO recipeDTO) throws RecipeException {
        recipeRepository.findByNameAndUserId(recipeDTO.getName(), getUserId())
                .map(recipeEntity -> {
                    throw new RecipeException("Error saving recipe", HttpStatus.BAD_REQUEST);
                }).orElseGet(() -> recipeRepository.save(toEntity(recipeDTO)));
    }

    public void delete(Long id) {
        recipeRepository.deleteByIdAndUserId(id, getUserId());
    }

    public void update(Long id, RecipeDTO recipeDTO) throws RecipeException {
        recipeRepository.findByIdAndUserId(id, getUserId())
                .map(entity -> {
                    updateRecipeEntity(recipeDTO, entity);
                    return recipeRepository.save(entity);
                }).orElseThrow(() ->new RecipeException("Error updating recipe", HttpStatus.BAD_REQUEST));
    }

    public List<RecipeDTO> findAllByUser()  {
        return recipeRepository.findByUserId(getUserId())
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<RecipeDTO> findByCriteria(Boolean isVegetarian, Integer portions, Set<String> includeIngredients,
                                                Set<String> excludeIngredients, String includeInstructions) {

        return recipeRepository.findByUserId(getUserId())
                .stream()
                .filter(recipeEntity -> filterSearch(isVegetarian, recipeEntity.getIsVegetarian()))
                .filter(recipeEntity -> filterSearch(portions, recipeEntity.getPortions()))
                .filter(recipeEntity -> filterIngredients(includeIngredients, recipeEntity.getIngredients(), Boolean.TRUE))
                .filter(recipeEntity -> filterIngredients(excludeIngredients, recipeEntity.getIngredients(), Boolean.FALSE))
                .filter(recipeEntity -> filterInstructionText(includeInstructions, recipeEntity.getInstructions()))
                .map(this::toDTO)
                .toList();
    }

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
        return userRepository.findById(getUserId()).orElseThrow(() -> new RuntimeException("user not found"));
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

    private boolean filterInstructionText(String filterInstruction, String instructions) {
        return Optional.ofNullable(filterInstruction)
                .map(fp -> instructions.contains(filterInstruction))
                .orElse(Boolean.TRUE);
    }

    private boolean filterSearch(Object filter, Object value) {
        return Optional.ofNullable(filter)
                .map(fp -> filter.equals(value))
                .orElse(Boolean.TRUE);
    }

}
