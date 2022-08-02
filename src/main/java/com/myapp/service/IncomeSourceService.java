package com.myapp.service;

import com.myapp.domain.IncomeSource;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link IncomeSource}.
 */
public interface IncomeSourceService {
    /**
     * Save a incomeSource.
     *
     * @param incomeSource the entity to save.
     * @return the persisted entity.
     */
    IncomeSource save(IncomeSource incomeSource);

    /**
     * Updates a incomeSource.
     *
     * @param incomeSource the entity to update.
     * @return the persisted entity.
     */
    IncomeSource update(IncomeSource incomeSource);

    /**
     * Partially updates a incomeSource.
     *
     * @param incomeSource the entity to update partially.
     * @return the persisted entity.
     */
    Optional<IncomeSource> partialUpdate(IncomeSource incomeSource);

    /**
     * Get all the incomeSources.
     *
     * @return the list of entities.
     */
    List<IncomeSource> findAll();

    /**
     * Get the "id" incomeSource.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<IncomeSource> findOne(Long id);

    /**
     * Delete the "id" incomeSource.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
