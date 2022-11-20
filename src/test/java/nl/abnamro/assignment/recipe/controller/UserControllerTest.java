package nl.abnamro.assignment.recipe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.abnamro.assignment.recipe.dto.UserDTO;
import nl.abnamro.assignment.recipe.exception.UserException;
import nl.abnamro.assignment.recipe.service.UserService;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private WebApplicationContext context;

    private ObjectMapper objectMapper;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void test_createUser_OK() throws Exception {
        UserDTO userDTO = UserDTO.builder().username("test").password("test").build();
        doNothing().when(userService).save(userDTO);
        mvc.perform(post("/user").with(user("USER_NAME").password("PASSWORD")).with(csrf())
                        .content(asJsonString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    public void test_createUser_ERROR() throws Exception {
        UserDTO userDTO = UserDTO.builder().username("test").password("test").build();
        doThrow(new UserException("error saving user", HttpStatus.INTERNAL_SERVER_ERROR))
                .when(userService).save(userDTO);
        mvc.perform(post("/user").with(user("USER_NAME").password("PASSWORD")).with(csrf())
                        .content(asJsonString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    private String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}