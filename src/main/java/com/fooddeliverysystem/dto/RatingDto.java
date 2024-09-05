package com.fooddeliverysystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RatingDto {
	private int rating;
	@NotBlank(message = "Review is mandatory")
	@Size(min = 10, max = 500, message = "Review must be between 10 and 500 characters")
	private String review;

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}
}
