package nl.abnamro.assignment.recipe.controller;


import lombok.AllArgsConstructor;
import nl.abnamro.assignment.recipe.dto.UserDTO;
import nl.abnamro.assignment.recipe.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/sayHi")
    public ResponseEntity<String> sayHi() {
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            return ResponseEntity.ok("Hi " + name);
        } catch(Exception e) {
            System.out.println("error");
        }
        return ResponseEntity.ok("NO NAME");
    }

    @GetMapping("/getUserName")
    public String getUserName() {
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            return "Hi " + name;
        } catch(Exception e) {
            System.out.println("error");
        }
        return null;
    }

    @PostMapping("/admin/add")
    public ResponseEntity<String> addUser(@RequestBody UserDTO userRequest) {

        try {
            if (userService.isUserPresent(userRequest.getUsername())){
                return ResponseEntity.badRequest().body("user exist !!!");
            }else {
                userService.save(userRequest);
            }
        }catch(Exception e) {
            System.out.println("error : " + e.getMessage());
        }

        return ResponseEntity.ok("saved !!!");

    }





}
