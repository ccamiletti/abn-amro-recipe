package nl.abnamro.assignment.recipe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.abnamro.assignment.recipe.dto.RecipeDTO;
import nl.abnamro.assignment.recipe.exception.RecipeException;
import nl.abnamro.assignment.recipe.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RecipeController.class)
public class RecipeControllerTest {

    private static final String USER_NAME = "test";
    private static final String PASSWORD = "password";

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @MockBean
    private RecipeService recipeService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void test_getAllRecipes_OK() throws Exception {
        List<RecipeDTO> recipeDTOList = List.of(RecipeDTO.builder().id(1L).name("recipe 1").isVegetarian(Boolean.TRUE).build());
        given(recipeService.findAllByUser()).willReturn(recipeDTOList);

        MvcResult result = mvc.perform(get("/recipe").with(user(USER_NAME).password(PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        RecipeDTO[] content = objectMapper.readValue(result.getResponse().getContentAsString(), RecipeDTO[].class);
        assertEquals(recipeDTOList.get(0).getId(), content[0].getId());
    }

    @Test
    public void test_getRecipeById_OK() throws Exception {
        RecipeDTO recipeDTO = RecipeDTO.builder().id(1L).name("recipe 1").isVegetarian(Boolean.TRUE).build();
        given(recipeService.findById(1L)).willReturn(recipeDTO);

        MvcResult result = mvc.perform(get("/recipe/1").with(user(USER_NAME).password(PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        RecipeDTO content = objectMapper.readValue(result.getResponse().getContentAsString(), RecipeDTO.class);
        assertEquals(content.getId(), recipeDTO.getId());
    }

    @Test
    public void test_getRecipeById_NOT_FOUND() throws Exception {
        given(recipeService.findById(1L)).willThrow(new RecipeException("Error getting recipe by id", HttpStatus.BAD_REQUEST));
        mvc.perform(
                get("/recipe/1").with(user(USER_NAME).password(PASSWORD)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_saveRecipe_OK() throws Exception {
        RecipeDTO recipeDTO = RecipeDTO.builder().name("recipe 1").isVegetarian(Boolean.TRUE).build();
        doNothing().when(recipeService).add(recipeDTO);
        mvc.perform(post("/recipe").with(user(USER_NAME).password(PASSWORD)).with(csrf())
                        .content(asJsonString(recipeDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    public void test_saveRecipe_BadRequest() throws Exception {
        RecipeDTO recipeDTO = RecipeDTO.builder().name("recipe 1").isVegetarian(Boolean.TRUE).build();
        doThrow(new RecipeException("Error saving recipe", HttpStatus.BAD_REQUEST)).when(recipeService).add(recipeDTO);
        mvc.perform(post("/recipe").with(user(USER_NAME).password(PASSWORD)).with(csrf())
                        .content(asJsonString(recipeDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_patchRecipe_OK() throws Exception {
        RecipeDTO recipeDTO = RecipeDTO.builder().portions(3).build();
        doNothing().when(recipeService).update(1L, recipeDTO);
        mvc.perform(patch("/recipe/1").with(user(USER_NAME).password(PASSWORD)).with(csrf())
                        .content("{\"portions\":\"3\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    public void test_patchRecipe_BadRequest() throws Exception {
        RecipeDTO recipeDTO = RecipeDTO.builder().portions(3).build();
        doThrow(new RecipeException("Error updating recipe", HttpStatus.BAD_REQUEST)).when(recipeService).update(1L, recipeDTO);
        mvc.perform(patch("/recipe/1").with(user(USER_NAME).password(PASSWORD)).with(csrf())
                        .content("{\"portions\":\"3\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}