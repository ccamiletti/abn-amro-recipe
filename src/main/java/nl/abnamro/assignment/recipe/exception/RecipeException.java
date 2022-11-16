package nl.abnamro.assignment.recipe.exception;

import org.springframework.http.HttpStatus;

public class RecipeException extends RuntimeException {

    public HttpStatus status;

    public RecipeException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
