package nl.abnamro.assignment.recipe.repository;

import nl.abnamro.assignment.recipe.entity.RecipeEntity;
import nl.abnamro.assignment.recipe.entity.UserEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RecipeRepositoryTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity userEntity;

    private RecipeEntity recipeEntity;

    @BeforeAll
    private void before() {
        userEntity = userRepository.save(UserEntity.builder().userName("test").password("password").build());
        recipeEntity = recipeRepository.save(RecipeEntity.builder().name("RecipeTest")
                .isVegetarian(Boolean.FALSE)
                .portions(10).user(userEntity).build());
    }

    @Test
    public void test_findById_OK() {
        Optional<RecipeEntity> recipe = recipeRepository.findById(recipeEntity.getId());
        assertEquals(recipe.get().getName(), "RecipeTest");
    }

    @Test
    public void test_findById_NOT_FOUND() {
        Optional<RecipeEntity> recipe = recipeRepository.findById(2L);
        assertEquals(recipe.isEmpty(), Boolean.TRUE);
    }

    @Test
    public void test_findByNameAndUserId_OK() {
        Optional<RecipeEntity> recipes = recipeRepository.findByNameAndUserId("RecipeTest", userEntity.getId());
        assertEquals(recipes.get().getName(), "RecipeTest");
    }

    @Test
    public void test_findByUserId_OK() {
        List<RecipeEntity> recipes = recipeRepository.findByUserId(userEntity.getId());
        assertEquals(recipes.size(), 1);
    }

}