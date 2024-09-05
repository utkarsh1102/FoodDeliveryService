package com.fooddeliverysystem.exception;

import java.util.List;

/**
 * Custom exception class for handling validation errors.
 * Extends RuntimeException to provide a specific exception type for validation failures.
 */
public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
 
	// List of validation error messages
    private final List<String> errors;

    /**
     * Constructor for ValidationException.
     *
     * @param errors A list of validation error messages to be associated with the exception.
     */
    public ValidationException(List<String> errors) {
        super("Validation failed"); // Set a default message for the exception
        this.errors = errors; // Initialize the list of errors
    }

    /**
     * Retrieves the list of validation error messages.
     *
     * @return A list of error messages associated with the validation failure.
     */
    public List<String> getErrors() {
        return errors;
    }
}