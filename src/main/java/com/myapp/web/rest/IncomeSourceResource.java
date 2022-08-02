package com.myapp.web.rest;

import com.myapp.domain.IncomeSource;
import com.myapp.repository.IncomeSourceRepository;
import com.myapp.service.IncomeSourceService;
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
 * REST controller for managing {@link com.myapp.domain.IncomeSource}.
 */
@RestController
@RequestMapping("/api")
public class IncomeSourceResource {

    private final Logger log = LoggerFactory.getLogger(IncomeSourceResource.class);

    private static final String ENTITY_NAME = "incomeSource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IncomeSourceService incomeSourceService;

    private final IncomeSourceRepository incomeSourceRepository;

    public IncomeSourceResource(IncomeSourceService incomeSourceService, IncomeSourceRepository incomeSourceRepository) {
        this.incomeSourceService = incomeSourceService;
        this.incomeSourceRepository = incomeSourceRepository;
    }

    /**
     * {@code POST  /income-sources} : Create a new incomeSource.
     *
     * @param incomeSource the incomeSource to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new incomeSource, or with status {@code 400 (Bad Request)} if the incomeSource has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/income-sources")
    public ResponseEntity<IncomeSource> createIncomeSource(@Valid @RequestBody IncomeSource incomeSource) throws URISyntaxException {
        log.debug("REST request to save IncomeSource : {}", incomeSource);
        if (incomeSource.getId() != null) {
            throw new BadRequestAlertException("A new incomeSource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IncomeSource result = incomeSourceService.save(incomeSource);
        return ResponseEntity
            .created(new URI("/api/income-sources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /income-sources/:id} : Updates an existing incomeSource.
     *
     * @param id the id of the incomeSource to save.
     * @param incomeSource the incomeSource to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated incomeSource,
     * or with status {@code 400 (Bad Request)} if the incomeSource is not valid,
     * or with status {@code 500 (Internal Server Error)} if the incomeSource couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/income-sources/{id}")
    public ResponseEntity<IncomeSource> updateIncomeSource(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IncomeSource incomeSource
    ) throws URISyntaxException {
        log.debug("REST request to update IncomeSource : {}, {}", id, incomeSource);
        if (incomeSource.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, incomeSource.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!incomeSourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IncomeSource result = incomeSourceService.update(incomeSource);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, incomeSource.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /income-sources/:id} : Partial updates given fields of an existing incomeSource, field will ignore if it is null
     *
     * @param id the id of the incomeSource to save.
     * @param incomeSource the incomeSource to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated incomeSource,
     * or with status {@code 400 (Bad Request)} if the incomeSource is not valid,
     * or with status {@code 404 (Not Found)} if the incomeSource is not found,
     * or with status {@code 500 (Internal Server Error)} if the incomeSource couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/income-sources/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IncomeSource> partialUpdateIncomeSource(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IncomeSource incomeSource
    ) throws URISyntaxException {
        log.debug("REST request to partial update IncomeSource partially : {}, {}", id, incomeSource);
        if (incomeSource.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, incomeSource.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!incomeSourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IncomeSource> result = incomeSourceService.partialUpdate(incomeSource);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, incomeSource.getId().toString())
        );
    }

    /**
     * {@code GET  /income-sources} : get all the incomeSources.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of incomeSources in body.
     */
    @GetMapping("/income-sources")
    public List<IncomeSource> getAllIncomeSources() {
        log.debug("REST request to get all IncomeSources");
        return incomeSourceService.findAll();
    }

    /**
     * {@code GET  /income-sources/:id} : get the "id" incomeSource.
     *
     * @param id the id of the incomeSource to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the incomeSource, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/income-sources/{id}")
    public ResponseEntity<IncomeSource> getIncomeSource(@PathVariable Long id) {
        log.debug("REST request to get IncomeSource : {}", id);
        Optional<IncomeSource> incomeSource = incomeSourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(incomeSource);
    }

    /**
     * {@code DELETE  /income-sources/:id} : delete the "id" incomeSource.
     *
     * @param id the id of the incomeSource to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/income-sources/{id}")
    public ResponseEntity<Void> deleteIncomeSource(@PathVariable Long id) {
        log.debug("REST request to delete IncomeSource : {}", id);
        incomeSourceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
