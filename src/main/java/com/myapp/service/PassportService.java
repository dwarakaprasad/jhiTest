package com.myapp.service;

import com.myapp.domain.Passport;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Passport}.
 */
public interface PassportService {
    /**
     * Save a passport.
     *
     * @param passport the entity to save.
     * @return the persisted entity.
     */
    Passport save(Passport passport);

    /**
     * Updates a passport.
     *
     * @param passport the entity to update.
     * @return the persisted entity.
     */
    Passport update(Passport passport);

    /**
     * Partially updates a passport.
     *
     * @param passport the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Passport> partialUpdate(Passport passport);

    /**
     * Get all the passports.
     *
     * @return the list of entities.
     */
    List<Passport> findAll();
    /**
     * Get all the Passport where Customer is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Passport> findAllWhereCustomerIsNull();

    /**
     * Get the "id" passport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Passport> findOne(Long id);

    /**
     * Delete the "id" passport.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
