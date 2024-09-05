package com.fooddeliverysystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class ExceptionHandler {
	/**
	 * Handles exceptions of type EntityNotFoundException.
	 *
	 * This method is invoked when an EntityNotFoundException is thrown in the application.
	 * It constructs a custom error response containing details about the error and the HTTP status.
	 *
	 * @param e The EntityNotFoundException that was thrown.
	 * @return A ResponseEntity containing a FoodDeliveryErrorResponse with error details and HTTP status NOT_FOUND (404).
	 */
	@org.springframework.web.bind.annotation.ExceptionHandler
	public ResponseEntity<FoodDeliveryErrorResponse> handleEntityNotFound(EntityNotFoundException e){
	    // Create an instance of FoodDeliveryErrorResponse to hold error details
	    FoodDeliveryErrorResponse error = new FoodDeliveryErrorResponse();
	    
	    // Set the HTTP status code to 404 (NOT_FOUND)
	    error.setStatus(HttpStatus.NOT_FOUND.value());
	    
	    // Set the error message from the exception
	    error.setMessage(e.getMessage());
	    
	    // Set the current timestamp of the error occurrence
	    error.setTimeStamp(System.currentTimeMillis());
	    
	    // Return a ResponseEntity with the error details and HTTP status NOT_FOUND
	    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	/**
	 * Handles InputMismatchException that occurs when there is a type mismatch in method arguments.
	 *
	 * This exception handler catches instances where a method argument does not match the expected type,
	 * such as when a non-numeric value is passed where a number is expected.
	 * It constructs a custom error response containing details about the error and sends it back to the client.
	 *
	 * @param e The InputMismatchException that was thrown.
	 * @return A ResponseEntity containing the error details and an HTTP status code indicating a bad request.
	 */
	@org.springframework.web.bind.annotation.ExceptionHandler
	public ResponseEntity<FoodDeliveryErrorResponse> handleInputMismatch(MethodArgumentTypeMismatchException e){
	    // Create a new instance of FoodDeliveryErrorResponse to store error details
	    FoodDeliveryErrorResponse error = new FoodDeliveryErrorResponse();
	    
	    // Set the HTTP status code for the response to BAD_REQUEST (400)
	    error.setStatus(HttpStatus.BAD_REQUEST.value());
	    
	    // Set the error message from the exception
	    error.setMessage(e.getMessage());
	    
	    // Set the current timestamp for when the error occurred
	    error.setTimeStamp(System.currentTimeMillis());
	    
	    // Return the error response wrapped in a ResponseEntity with BAD_REQUEST status
	    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handles validation exceptions thrown by the application.
	 *
	 * This method catches exceptions of type ValidationException and constructs a detailed
	 * error response to be sent back to the client. It provides information about the validation
	 * errors, the HTTP status, and the timestamp of the error occurrence.
	 *
	 * @param e The ValidationException instance containing validation error details.
	 * @return A ResponseEntity containing a FoodDeliveryErrorResponse with error details and HTTP status.
	 */
	@org.springframework.web.bind.annotation.ExceptionHandler
	public ResponseEntity<FoodDeliveryErrorResponse> handleValidationException(ValidationException e) {
	    // Build a response body containing validation error messages
	    FoodDeliveryErrorResponse error = new FoodDeliveryErrorResponse();
	    error.setMessage("There are some validation errors"); // Set a generic message for validation errors
	    error.setStatus(HttpStatus.BAD_REQUEST.value()); // Set the HTTP status code for bad request (400)
	    error.setErrors(e.getErrors()); // Set the list of validation errors from the exception
	    error.setTimeStamp(System.currentTimeMillis()); // Set the timestamp of when the error occurred

	    // Return the response entity with error messages and HTTP status
	    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
}
