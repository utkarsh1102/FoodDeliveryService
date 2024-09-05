package com.fooddeliverysystem.exception;

import java.util.List;

/**
 * Represents an error response structure for the Food Delivery application.
 * This class is used to encapsulate the details of an error response, including
 * the HTTP status, error message(s), a list of specific errors, and a timestamp
 * of when the error occurred.
 */
public class FoodDeliveryErrorResponse {

    /**
     * The HTTP status code associated with the error.
     */
    private int status;

    /**
     * A descriptive message about the error.
     */
    private String message;

    /**
     * A list of specific errors or validation messages related to the request.
     */
    private List<String> errors;

    /**
     * The timestamp indicating when the error occurred.
     */
    private long timeStamp;

    /**
     * Gets the HTTP status code associated with the error.
     *
     * @return The HTTP status code.
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets the HTTP status code associated with the error.
     *
     * @param status The HTTP status code to set.
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Gets the list of specific errors or validation messages related to the request.
     *
     * @return A list of error messages.
     */
    public List<String> getErrors() {
        return errors;
    }

    /**
     * Sets the list of specific errors or validation messages related to the request.
     *
     * @param errors The list of error messages to set.
     */
    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    /**
     * Gets the descriptive message about the error.
     *
     * @return The error message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the descriptive message about the error.
     *
     * @param message The error message to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the timestamp indicating when the error occurred.
     *
     * @return The timestamp of the error.
     */
    public long getTimeStamp() {
        return timeStamp;
    }

    /**
     * Sets the timestamp indicating when the error occurred.
     *
     * @param timeStamp The timestamp to set.
     */
    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
