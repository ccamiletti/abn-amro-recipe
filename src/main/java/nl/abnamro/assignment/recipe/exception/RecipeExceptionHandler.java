package nl.abnamro.assignment.recipe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class RecipeExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RecipeException.class)
    public ResponseEntity<RecipeExceptionResponse> createRecipeErrorMessage(RecipeException recipeException) {
        return new ResponseEntity<>(
                createResponse(recipeException.getMessage(), Instant.now(), recipeException.status), recipeException.status
        );
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<RecipeExceptionResponse> createUserErrorMessage(UserException userException) {
        return new ResponseEntity<>(
                createResponse(userException.getMessage(), Instant.now(), userException.status), userException.status);
    }

    private RecipeExceptionResponse createResponse(String message, Instant instant, HttpStatus status) {
        return RecipeExceptionResponse.builder()
                        .message(message)
                        .timestamp(instant)
                        .status(status).build();
    }



}
