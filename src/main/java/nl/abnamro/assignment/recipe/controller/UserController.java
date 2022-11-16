package nl.abnamro.assignment.recipe.controller;


import lombok.AllArgsConstructor;
import nl.abnamro.assignment.recipe.dto.UserDTO;
import nl.abnamro.assignment.recipe.exception.UserException;
import nl.abnamro.assignment.recipe.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> addUser(@RequestBody UserDTO userRequest) throws UserException {
        userService.save(userRequest);
        return ResponseEntity.accepted().build();
    }

}
