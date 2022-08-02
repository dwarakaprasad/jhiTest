package com.myapp.service;

import com.myapp.domain.Assets;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Assets}.
 */
public interface AssetsService {
    /**
     * Save a assets.
     *
     * @param assets the entity to save.
     * @return the persisted entity.
     */
    Assets save(Assets assets);

    /**
     * Updates a assets.
     *
     * @param assets the entity to update.
     * @return the persisted entity.
     */
    Assets update(Assets assets);

    /**
     * Partially updates a assets.
     *
     * @param assets the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Assets> partialUpdate(Assets assets);

    /**
     * Get all the assets.
     *
     * @return the list of entities.
     */
    List<Assets> findAll();

    /**
     * Get the "id" assets.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Assets> findOne(Long id);

    /**
     * Delete the "id" assets.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
