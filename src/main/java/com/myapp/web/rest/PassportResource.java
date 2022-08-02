package com.myapp.web.rest;

import com.myapp.domain.Passport;
import com.myapp.repository.PassportRepository;
import com.myapp.service.PassportService;
import com.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.myapp.domain.Passport}.
 */
@RestController
@RequestMapping("/api")
public class PassportResource {

    private final Logger log = LoggerFactory.getLogger(PassportResource.class);

    private static final String ENTITY_NAME = "passport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PassportService passportService;

    private final PassportRepository passportRepository;

    public PassportResource(PassportService passportService, PassportRepository passportRepository) {
        this.passportService = passportService;
        this.passportRepository = passportRepository;
    }

    /**
     * {@code POST  /passports} : Create a new passport.
     *
     * @param passport the passport to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new passport, or with status {@code 400 (Bad Request)} if the passport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/passports")
    public ResponseEntity<Passport> createPassport(@Valid @RequestBody Passport passport) throws URISyntaxException {
        log.debug("REST request to save Passport : {}", passport);
        if (passport.getId() != null) {
            throw new BadRequestAlertException("A new passport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Passport result = passportService.save(passport);
        return ResponseEntity
            .created(new URI("/api/passports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /passports/:id} : Updates an existing passport.
     *
     * @param id the id of the passport to save.
     * @param passport the passport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated passport,
     * or with status {@code 400 (Bad Request)} if the passport is not valid,
     * or with status {@code 500 (Internal Server Error)} if the passport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/passports/{id}")
    public ResponseEntity<Passport> updatePassport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Passport passport
    ) throws URISyntaxException {
        log.debug("REST request to update Passport : {}, {}", id, passport);
        if (passport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, passport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!passportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Passport result = passportService.update(passport);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, passport.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /passports/:id} : Partial updates given fields of an existing passport, field will ignore if it is null
     *
     * @param id the id of the passport to save.
     * @param passport the passport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated passport,
     * or with status {@code 400 (Bad Request)} if the passport is not valid,
     * or with status {@code 404 (Not Found)} if the passport is not found,
     * or with status {@code 500 (Internal Server Error)} if the passport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/passports/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Passport> partialUpdatePassport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Passport passport
    ) throws URISyntaxException {
        log.debug("REST request to partial update Passport partially : {}, {}", id, passport);
        if (passport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, passport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!passportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Passport> result = passportService.partialUpdate(passport);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, passport.getId().toString())
        );
    }

    /**
     * {@code GET  /passports} : get all the passports.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of passports in body.
     */
    @GetMapping("/passports")
    public List<Passport> getAllPassports(@RequestParam(required = false) String filter) {
        if ("customer-is-null".equals(filter)) {
            log.debug("REST request to get all Passports where customer is null");
            return passportService.findAllWhereCustomerIsNull();
        }
        log.debug("REST request to get all Passports");
        return passportService.findAll();
    }

    /**
     * {@code GET  /passports/:id} : get the "id" passport.
     *
     * @param id the id of the passport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the passport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/passports/{id}")
    public ResponseEntity<Passport> getPassport(@PathVariable Long id) {
        log.debug("REST request to get Passport : {}", id);
        Optional<Passport> passport = passportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(passport);
    }

    /**
     * {@code DELETE  /passports/:id} : delete the "id" passport.
     *
     * @param id the id of the passport to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/passports/{id}")
    public ResponseEntity<Void> deletePassport(@PathVariable Long id) {
        log.debug("REST request to delete Passport : {}", id);
        passportService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
