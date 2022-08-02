package com.myapp.service;

import com.myapp.domain.Employer;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Employer}.
 */
public interface EmployerService {
    /**
     * Save a employer.
     *
     * @param employer the entity to save.
     * @return the persisted entity.
     */
    Employer save(Employer employer);

    /**
     * Updates a employer.
     *
     * @param employer the entity to update.
     * @return the persisted entity.
     */
    Employer update(Employer employer);

    /**
     * Partially updates a employer.
     *
     * @param employer the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Employer> partialUpdate(Employer employer);

    /**
     * Get all the employers.
     *
     * @return the list of entities.
     */
    List<Employer> findAll();

    /**
     * Get the "id" employer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Employer> findOne(Long id);

    /**
     * Delete the "id" employer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
