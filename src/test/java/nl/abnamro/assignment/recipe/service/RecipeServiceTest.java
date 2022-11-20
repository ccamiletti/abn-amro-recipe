package nl.abnamro.assignment.recipe.service;

import nl.abnamro.assignment.recipe.dto.RecipeDTO;
import nl.abnamro.assignment.recipe.dto.UserDTO;
import nl.abnamro.assignment.recipe.entity.RecipeEntity;
import nl.abnamro.assignment.recipe.entity.UserEntity;
import nl.abnamro.assignment.recipe.exception.RecipeException;
import nl.abnamro.assignment.recipe.repository.RecipeRepository;
import nl.abnamro.assignment.recipe.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
public class RecipeServiceTest {

    @InjectMocks
    private RecipeService recipeService;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void onSetUp() {
        Authentication auth = new UsernamePasswordAuthenticationToken(UserDTO.builder().id(1L).build(),null);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    public void test_findById_OK() {
        RecipeEntity recipeEntity = RecipeEntity.builder().id(1L).name("recipe 1").build();
        given(recipeRepository.findByIdAndUserId(1L, 1l)).willReturn(Optional.of(recipeEntity));
        RecipeDTO recipeDTO = recipeService.findById(1L);
        assertEquals(recipeEntity.getId(), recipeDTO.getId());
    }

    @Test
    public void test_findById_NOT_FOUND() {
        given(recipeRepository.findByIdAndUserId(1L, 1l)).willReturn(Optional.empty());
        try {
            recipeService.findById(1L);
        }catch(RecipeException exception) {
            assertEquals(exception.getMessage(), "Error getting recipe by id");
            assertEquals(exception.status, HttpStatus.BAD_REQUEST);
        }
    }

    @Test
    public void test_addRecipe_OK() {
        RecipeEntity recipeEntity = RecipeEntity.builder().name("recipe 1").build();
        RecipeEntity recipeEntityResponse = RecipeEntity.builder().id(1L).name("recipe 1").build();
        RecipeDTO recipeDTO = RecipeDTO.builder().name("recipe 1").build();
        UserEntity userEntity = UserEntity.builder().id(1L).userName("test").build();
        given(userRepository.findById(1L)).willReturn(Optional.of(userEntity));
        given(recipeRepository.findByNameAndUserId(recipeDTO.getName(), 1L)).willReturn(Optional.empty());
        given(recipeRepository.save(recipeEntity)).willReturn(recipeEntityResponse);
        recipeService.add(recipeDTO);
    }

    @Test
    public void test_addRecipe_BAD_REQUEST() {
        RecipeEntity recipeEntityResponse = RecipeEntity.builder().id(1L).name("recipe 1").build();
        RecipeDTO recipeDTORequest = RecipeDTO.builder().name("recipe 1").build();
        given(recipeRepository.findByNameAndUserId(recipeDTORequest.getName(), 1L)).willReturn(Optional.of(recipeEntityResponse));
        try {
            recipeService.add(recipeDTORequest);
        }catch(RecipeException recipeException) {
            assertEquals(recipeException.getMessage(), "Error saving recipe");
            assertEquals(recipeException.status, HttpStatus.BAD_REQUEST);
        }
    }

    @Test
    public void test_updateRecipe_OK() {
        RecipeEntity recipeEntityResponse = RecipeEntity.builder().id(1L).name("recipe 1").build();
        RecipeDTO recipeDTO = RecipeDTO.builder().name("recipe 1").build();
        given(recipeRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(recipeEntityResponse));
        given(recipeRepository.save(recipeEntityResponse)).willReturn(recipeEntityResponse);
        recipeService.update(1L, recipeDTO);
    }

    @Test
    public void test_updateRecipe_BAD_REQUEST() {
        RecipeDTO recipeDTO = RecipeDTO.builder().name("recipe 1").build();
        given(recipeRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.empty());
        try {
            recipeService.update(1L, recipeDTO);
        }catch(RecipeException recipeException) {
            assertEquals(recipeException.getMessage(), "Error updating recipe");
            assertEquals(recipeException.status, HttpStatus.BAD_REQUEST);
        }
    }
}