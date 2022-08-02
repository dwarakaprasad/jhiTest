package com.myapp.web.rest;

import com.myapp.domain.Assets;
import com.myapp.repository.AssetsRepository;
import com.myapp.service.AssetsService;
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
 * REST controller for managing {@link com.myapp.domain.Assets}.
 */
@RestController
@RequestMapping("/api")
public class AssetsResource {

    private final Logger log = LoggerFactory.getLogger(AssetsResource.class);

    private static final String ENTITY_NAME = "assets";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssetsService assetsService;

    private final AssetsRepository assetsRepository;

    public AssetsResource(AssetsService assetsService, AssetsRepository assetsRepository) {
        this.assetsService = assetsService;
        this.assetsRepository = assetsRepository;
    }

    /**
     * {@code POST  /assets} : Create a new assets.
     *
     * @param assets the assets to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assets, or with status {@code 400 (Bad Request)} if the assets has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/assets")
    public ResponseEntity<Assets> createAssets(@Valid @RequestBody Assets assets) throws URISyntaxException {
        log.debug("REST request to save Assets : {}", assets);
        if (assets.getId() != null) {
            throw new BadRequestAlertException("A new assets cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Assets result = assetsService.save(assets);
        return ResponseEntity
            .created(new URI("/api/assets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /assets/:id} : Updates an existing assets.
     *
     * @param id the id of the assets to save.
     * @param assets the assets to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assets,
     * or with status {@code 400 (Bad Request)} if the assets is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assets couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/assets/{id}")
    public ResponseEntity<Assets> updateAssets(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Assets assets
    ) throws URISyntaxException {
        log.debug("REST request to update Assets : {}, {}", id, assets);
        if (assets.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assets.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Assets result = assetsService.update(assets);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assets.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /assets/:id} : Partial updates given fields of an existing assets, field will ignore if it is null
     *
     * @param id the id of the assets to save.
     * @param assets the assets to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assets,
     * or with status {@code 400 (Bad Request)} if the assets is not valid,
     * or with status {@code 404 (Not Found)} if the assets is not found,
     * or with status {@code 500 (Internal Server Error)} if the assets couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/assets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Assets> partialUpdateAssets(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Assets assets
    ) throws URISyntaxException {
        log.debug("REST request to partial update Assets partially : {}, {}", id, assets);
        if (assets.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assets.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Assets> result = assetsService.partialUpdate(assets);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assets.getId().toString())
        );
    }

    /**
     * {@code GET  /assets} : get all the assets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assets in body.
     */
    @GetMapping("/assets")
    public List<Assets> getAllAssets() {
        log.debug("REST request to get all Assets");
        return assetsService.findAll();
    }

    /**
     * {@code GET  /assets/:id} : get the "id" assets.
     *
     * @param id the id of the assets to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assets, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/assets/{id}")
    public ResponseEntity<Assets> getAssets(@PathVariable Long id) {
        log.debug("REST request to get Assets : {}", id);
        Optional<Assets> assets = assetsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assets);
    }

    /**
     * {@code DELETE  /assets/:id} : delete the "id" assets.
     *
     * @param id the id of the assets to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/assets/{id}")
    public ResponseEntity<Void> deleteAssets(@PathVariable Long id) {
        log.debug("REST request to delete Assets : {}", id);
        assetsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
