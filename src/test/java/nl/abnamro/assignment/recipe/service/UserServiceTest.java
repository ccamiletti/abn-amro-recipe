package nl.abnamro.assignment.recipe.service;

import nl.abnamro.assignment.recipe.dto.UserDTO;
import nl.abnamro.assignment.recipe.entity.UserEntity;
import nl.abnamro.assignment.recipe.exception.UserException;
import nl.abnamro.assignment.recipe.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    private final static String USER_NAME = "test";

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void test_findByUserName_OK() {
        given(userRepository.findByUserName(USER_NAME))
                .willReturn(Optional.of(UserEntity.builder().userName(USER_NAME).id(1L).build()));
        UserDTO userDTO = userService.findByUserName(USER_NAME);
        assertEquals(userDTO.getUsername(), USER_NAME);
    }

    @Test
    public void test_findByUserName_EXCEPTION() {
        given(userRepository.findByUserName(USER_NAME)).willReturn(Optional.empty());
        try {
            userService.findByUserName(USER_NAME);
        }catch(UserException userException) {
            assertEquals(userException.getMessage(), "User not found");
        }
    }
}