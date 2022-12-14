package com.myapp.service;

import com.myapp.domain.Applicant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Applicant}.
 */
public interface ApplicantService {
    /**
     * Save a applicant.
     *
     * @param applicant the entity to save.
     * @return the persisted entity.
     */
    Applicant save(Applicant applicant);

    /**
     * Updates a applicant.
     *
     * @param applicant the entity to update.
     * @return the persisted entity.
     */
    Applicant update(Applicant applicant);

    /**
     * Partially updates a applicant.
     *
     * @param applicant the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Applicant> partialUpdate(Applicant applicant);

    /**
     * Get all the applicants.
     *
     * @return the list of entities.
     */
    List<Applicant> findAll();

    /**
     * Get all the applicants with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Applicant> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" applicant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Applicant> findOne(Long id);

    /**
     * Delete the "id" applicant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
