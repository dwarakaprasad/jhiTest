package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.Assets;
import com.myapp.domain.enumeration.ASSETTYPE;
import com.myapp.repository.AssetsRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AssetsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AssetsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ASSETTYPE DEFAULT_ASSET_TYPE = ASSETTYPE.MOVABLE;
    private static final ASSETTYPE UPDATED_ASSET_TYPE = ASSETTYPE.CASH;

    private static final String ENTITY_API_URL = "/api/assets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssetsRepository assetsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssetsMockMvc;

    private Assets assets;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assets createEntity(EntityManager em) {
        Assets assets = new Assets().name(DEFAULT_NAME).assetType(DEFAULT_ASSET_TYPE);
        return assets;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assets createUpdatedEntity(EntityManager em) {
        Assets assets = new Assets().name(UPDATED_NAME).assetType(UPDATED_ASSET_TYPE);
        return assets;
    }

    @BeforeEach
    public void initTest() {
        assets = createEntity(em);
    }

    @Test
    @Transactional
    void createAssets() throws Exception {
        int databaseSizeBeforeCreate = assetsRepository.findAll().size();
        // Create the Assets
        restAssetsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assets)))
            .andExpect(status().isCreated());

        // Validate the Assets in the database
        List<Assets> assetsList = assetsRepository.findAll();
        assertThat(assetsList).hasSize(databaseSizeBeforeCreate + 1);
        Assets testAssets = assetsList.get(assetsList.size() - 1);
        assertThat(testAssets.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAssets.getAssetType()).isEqualTo(DEFAULT_ASSET_TYPE);
    }

    @Test
    @Transactional
    void createAssetsWithExistingId() throws Exception {
        // Create the Assets with an existing ID
        assets.setId(1L);

        int databaseSizeBeforeCreate = assetsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssetsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assets)))
            .andExpect(status().isBadRequest());

        // Validate the Assets in the database
        List<Assets> assetsList = assetsRepository.findAll();
        assertThat(assetsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetsRepository.findAll().size();
        // set the field null
        assets.setName(null);

        // Create the Assets, which fails.

        restAssetsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assets)))
            .andExpect(status().isBadRequest());

        List<Assets> assetsList = assetsRepository.findAll();
        assertThat(assetsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAssetTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetsRepository.findAll().size();
        // set the field null
        assets.setAssetType(null);

        // Create the Assets, which fails.

        restAssetsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assets)))
            .andExpect(status().isBadRequest());

        List<Assets> assetsList = assetsRepository.findAll();
        assertThat(assetsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAssets() throws Exception {
        // Initialize the database
        assetsRepository.saveAndFlush(assets);

        // Get all the assetsList
        restAssetsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assets.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].assetType").value(hasItem(DEFAULT_ASSET_TYPE.toString())));
    }

    @Test
    @Transactional
    void getAssets() throws Exception {
        // Initialize the database
        assetsRepository.saveAndFlush(assets);

        // Get the assets
        restAssetsMockMvc
            .perform(get(ENTITY_API_URL_ID, assets.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assets.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.assetType").value(DEFAULT_ASSET_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAssets() throws Exception {
        // Get the assets
        restAssetsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAssets() throws Exception {
        // Initialize the database
        assetsRepository.saveAndFlush(assets);

        int databaseSizeBeforeUpdate = assetsRepository.findAll().size();

        // Update the assets
        Assets updatedAssets = assetsRepository.findById(assets.getId()).get();
        // Disconnect from session so that the updates on updatedAssets are not directly saved in db
        em.detach(updatedAssets);
        updatedAssets.name(UPDATED_NAME).assetType(UPDATED_ASSET_TYPE);

        restAssetsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAssets.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAssets))
            )
            .andExpect(status().isOk());

        // Validate the Assets in the database
        List<Assets> assetsList = assetsRepository.findAll();
        assertThat(assetsList).hasSize(databaseSizeBeforeUpdate);
        Assets testAssets = assetsList.get(assetsList.size() - 1);
        assertThat(testAssets.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAssets.getAssetType()).isEqualTo(UPDATED_ASSET_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingAssets() throws Exception {
        int databaseSizeBeforeUpdate = assetsRepository.findAll().size();
        assets.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assets.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assets))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assets in the database
        List<Assets> assetsList = assetsRepository.findAll();
        assertThat(assetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssets() throws Exception {
        int databaseSizeBeforeUpdate = assetsRepository.findAll().size();
        assets.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assets))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assets in the database
        List<Assets> assetsList = assetsRepository.findAll();
        assertThat(assetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssets() throws Exception {
        int databaseSizeBeforeUpdate = assetsRepository.findAll().size();
        assets.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assets)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Assets in the database
        List<Assets> assetsList = assetsRepository.findAll();
        assertThat(assetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAssetsWithPatch() throws Exception {
        // Initialize the database
        assetsRepository.saveAndFlush(assets);

        int databaseSizeBeforeUpdate = assetsRepository.findAll().size();

        // Update the assets using partial update
        Assets partialUpdatedAssets = new Assets();
        partialUpdatedAssets.setId(assets.getId());

        partialUpdatedAssets.name(UPDATED_NAME);

        restAssetsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssets.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssets))
            )
            .andExpect(status().isOk());

        // Validate the Assets in the database
        List<Assets> assetsList = assetsRepository.findAll();
        assertThat(assetsList).hasSize(databaseSizeBeforeUpdate);
        Assets testAssets = assetsList.get(assetsList.size() - 1);
        assertThat(testAssets.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAssets.getAssetType()).isEqualTo(DEFAULT_ASSET_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateAssetsWithPatch() throws Exception {
        // Initialize the database
        assetsRepository.saveAndFlush(assets);

        int databaseSizeBeforeUpdate = assetsRepository.findAll().size();

        // Update the assets using partial update
        Assets partialUpdatedAssets = new Assets();
        partialUpdatedAssets.setId(assets.getId());

        partialUpdatedAssets.name(UPDATED_NAME).assetType(UPDATED_ASSET_TYPE);

        restAssetsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssets.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssets))
            )
            .andExpect(status().isOk());

        // Validate the Assets in the database
        List<Assets> assetsList = assetsRepository.findAll();
        assertThat(assetsList).hasSize(databaseSizeBeforeUpdate);
        Assets testAssets = assetsList.get(assetsList.size() - 1);
        assertThat(testAssets.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAssets.getAssetType()).isEqualTo(UPDATED_ASSET_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingAssets() throws Exception {
        int databaseSizeBeforeUpdate = assetsRepository.findAll().size();
        assets.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assets.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assets))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assets in the database
        List<Assets> assetsList = assetsRepository.findAll();
        assertThat(assetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssets() throws Exception {
        int databaseSizeBeforeUpdate = assetsRepository.findAll().size();
        assets.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assets))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assets in the database
        List<Assets> assetsList = assetsRepository.findAll();
        assertThat(assetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssets() throws Exception {
        int databaseSizeBeforeUpdate = assetsRepository.findAll().size();
        assets.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(assets)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Assets in the database
        List<Assets> assetsList = assetsRepository.findAll();
        assertThat(assetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAssets() throws Exception {
        // Initialize the database
        assetsRepository.saveAndFlush(assets);

        int databaseSizeBeforeDelete = assetsRepository.findAll().size();

        // Delete the assets
        restAssetsMockMvc
            .perform(delete(ENTITY_API_URL_ID, assets.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Assets> assetsList = assetsRepository.findAll();
        assertThat(assetsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
