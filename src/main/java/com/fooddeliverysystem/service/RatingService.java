package com.fooddeliverysystem.service;

import com.fooddeliverysystem.entity.Rating;

/**
 * Service interface for managing ratings. This interface defines the operations
 * related to handling ratings, including retrieving the most recent rating and
 * saving a new or updated rating.
 */
public interface RatingService {

	/**
	 * Retrieves the most recently added rating from the database.
	 *
	 * This method queries the database to find the rating with the highest ID,
	 * which represents the most recently added rating. It is expected to return the
	 * latest Rating object or null if no ratings are present.
	 *
	 * @return The most recently added Rating object, or null if no ratings are
	 *         found.
	 */
	Rating findLastRecord();

	/**
	 * Saves a rating to the database.
	 *
	 * This method persists the given Rating object to the database. If the Rating
	 * already exists, it will be updated; otherwise, a new record will be created.
	 * The saved Rating object will include any updates or new database-generated
	 * fields.
	 *
	 * @param rating The Rating object to be saved.
	 * @return The saved Rating object.
	 */
	Rating save(Rating rating);
}
