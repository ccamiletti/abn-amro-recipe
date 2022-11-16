package nl.abnamro.assignment.recipe.exception;

import org.springframework.http.HttpStatus;

public class UserException extends Exception {

    public HttpStatus status;
    public UserException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}