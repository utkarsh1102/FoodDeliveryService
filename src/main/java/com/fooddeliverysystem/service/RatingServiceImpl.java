package com.fooddeliverysystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fooddeliverysystem.dao.RatingDao;
import com.fooddeliverysystem.entity.Rating;

@Service
public class RatingServiceImpl implements RatingService {

	private static final Logger logger = LoggerFactory.getLogger(RatingServiceImpl.class);
	private RatingDao ratingDao;

	@Autowired
	public RatingServiceImpl(RatingDao ratingDao) {
		super();
		this.ratingDao = ratingDao;
	}

	/**
	 * Retrieve the most recently added rating from the database.
	 * 
	 * @return The most recently added `Rating`, or null if no ratings are present.
	 */
	@Override
	public Rating findLastRecord() {
		logger.info("Fetching the most recent rating");

		Rating latestRating = ratingDao.findFirstByOrderByRatingIdDesc();

		if (latestRating == null) {
			logger.warn("No ratings found in the database");
		} else {
			logger.info("Most recent rating retrieved with ID: {}", latestRating.getRatingId());
		}
		return latestRating;
	}

	/**
	 * Save a rating to the database.
	 * 
	 * @param rating The rating object to be saved.
	 * @return The saved `Rating` object, which may include updated details such as
	 *         a generated ID.
	 */
	@Override
	public Rating save(Rating rating) {
		logger.info("Attempting to save rating: {}", rating);

		Rating savedRating = ratingDao.save(rating);

		logger.info("Rating saved successfully with ID: {}", savedRating.getRatingId());

		return savedRating;
	}
}
