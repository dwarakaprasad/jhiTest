package com.myapp.service;

import com.myapp.domain.PaymentInfo;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link PaymentInfo}.
 */
public interface PaymentInfoService {
    /**
     * Save a paymentInfo.
     *
     * @param paymentInfo the entity to save.
     * @return the persisted entity.
     */
    PaymentInfo save(PaymentInfo paymentInfo);

    /**
     * Updates a paymentInfo.
     *
     * @param paymentInfo the entity to update.
     * @return the persisted entity.
     */
    PaymentInfo update(PaymentInfo paymentInfo);

    /**
     * Partially updates a paymentInfo.
     *
     * @param paymentInfo the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PaymentInfo> partialUpdate(PaymentInfo paymentInfo);

    /**
     * Get all the paymentInfos.
     *
     * @return the list of entities.
     */
    List<PaymentInfo> findAll();

    /**
     * Get the "id" paymentInfo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaymentInfo> findOne(Long id);

    /**
     * Delete the "id" paymentInfo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
