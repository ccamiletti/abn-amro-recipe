package nl.abnamro.assignment.recipe.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
@AllArgsConstructor
@Builder
public class RecipeExceptionResponse {

    private Instant timestamp;
    private HttpStatus status;
    private String message;

}
