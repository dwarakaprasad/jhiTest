package com.myapp.web.rest;

import com.myapp.domain.PaymentInfo;
import com.myapp.repository.PaymentInfoRepository;
import com.myapp.service.PaymentInfoService;
import com.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.myapp.domain.PaymentInfo}.
 */
@RestController
@RequestMapping("/api")
public class PaymentInfoResource {

    private final Logger log = LoggerFactory.getLogger(PaymentInfoResource.class);

    private static final String ENTITY_NAME = "paymentInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentInfoService paymentInfoService;

    private final PaymentInfoRepository paymentInfoRepository;

    public PaymentInfoResource(PaymentInfoService paymentInfoService, PaymentInfoRepository paymentInfoRepository) {
        this.paymentInfoService = paymentInfoService;
        this.paymentInfoRepository = paymentInfoRepository;
    }

    /**
     * {@code POST  /payment-infos} : Create a new paymentInfo.
     *
     * @param paymentInfo the paymentInfo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentInfo, or with status {@code 400 (Bad Request)} if the paymentInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payment-infos")
    public ResponseEntity<PaymentInfo> createPaymentInfo(@Valid @RequestBody PaymentInfo paymentInfo) throws URISyntaxException {
        log.debug("REST request to save PaymentInfo : {}", paymentInfo);
        if (paymentInfo.getId() != null) {
            throw new BadRequestAlertException("A new paymentInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaymentInfo result = paymentInfoService.save(paymentInfo);
        return ResponseEntity
            .created(new URI("/api/payment-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /payment-infos/:id} : Updates an existing paymentInfo.
     *
     * @param id the id of the paymentInfo to save.
     * @param paymentInfo the paymentInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentInfo,
     * or with status {@code 400 (Bad Request)} if the paymentInfo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payment-infos/{id}")
    public ResponseEntity<PaymentInfo> updatePaymentInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentInfo paymentInfo
    ) throws URISyntaxException {
        log.debug("REST request to update PaymentInfo : {}, {}", id, paymentInfo);
        if (paymentInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentInfo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PaymentInfo result = paymentInfoService.update(paymentInfo);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentInfo.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /payment-infos/:id} : Partial updates given fields of an existing paymentInfo, field will ignore if it is null
     *
     * @param id the id of the paymentInfo to save.
     * @param paymentInfo the paymentInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentInfo,
     * or with status {@code 400 (Bad Request)} if the paymentInfo is not valid,
     * or with status {@code 404 (Not Found)} if the paymentInfo is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/payment-infos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentInfo> partialUpdatePaymentInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentInfo paymentInfo
    ) throws URISyntaxException {
        log.debug("REST request to partial update PaymentInfo partially : {}, {}", id, paymentInfo);
        if (paymentInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentInfo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentInfo> result = paymentInfoService.partialUpdate(paymentInfo);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentInfo.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-infos} : get all the paymentInfos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentInfos in body.
     */
    @GetMapping("/payment-infos")
    public List<PaymentInfo> getAllPaymentInfos() {
        log.debug("REST request to get all PaymentInfos");
        return paymentInfoService.findAll();
    }

    /**
     * {@code GET  /payment-infos/:id} : get the "id" paymentInfo.
     *
     * @param id the id of the paymentInfo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentInfo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payment-infos/{id}")
    public ResponseEntity<PaymentInfo> getPaymentInfo(@PathVariable Long id) {
        log.debug("REST request to get PaymentInfo : {}", id);
        Optional<PaymentInfo> paymentInfo = paymentInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentInfo);
    }

    /**
     * {@code DELETE  /payment-infos/:id} : delete the "id" paymentInfo.
     *
     * @param id the id of the paymentInfo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payment-infos/{id}")
    public ResponseEntity<Void> deletePaymentInfo(@PathVariable Long id) {
        log.debug("REST request to delete PaymentInfo : {}", id);
        paymentInfoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
