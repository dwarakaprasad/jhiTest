package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.IncomeSource;
import com.myapp.domain.enumeration.INCOMETYPE;
import com.myapp.repository.IncomeSourceRepository;
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
 * Integration tests for the {@link IncomeSourceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IncomeSourceResourceIT {

    private static final INCOMETYPE DEFAULT_INCOME_TYPE = INCOMETYPE.JOB;
    private static final INCOMETYPE UPDATED_INCOME_TYPE = INCOMETYPE.BUSINESS;

    private static final Double DEFAULT_INCOME_AMOUNT = 1D;
    private static final Double UPDATED_INCOME_AMOUNT = 2D;

    private static final Long DEFAULT_DURATION = 1L;
    private static final Long UPDATED_DURATION = 2L;

    private static final String ENTITY_API_URL = "/api/income-sources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IncomeSourceRepository incomeSourceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIncomeSourceMockMvc;

    private IncomeSource incomeSource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IncomeSource createEntity(EntityManager em) {
        IncomeSource incomeSource = new IncomeSource()
            .incomeType(DEFAULT_INCOME_TYPE)
            .incomeAmount(DEFAULT_INCOME_AMOUNT)
            .duration(DEFAULT_DURATION);
        return incomeSource;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IncomeSource createUpdatedEntity(EntityManager em) {
        IncomeSource incomeSource = new IncomeSource()
            .incomeType(UPDATED_INCOME_TYPE)
            .incomeAmount(UPDATED_INCOME_AMOUNT)
            .duration(UPDATED_DURATION);
        return incomeSource;
    }

    @BeforeEach
    public void initTest() {
        incomeSource = createEntity(em);
    }

    @Test
    @Transactional
    void createIncomeSource() throws Exception {
        int databaseSizeBeforeCreate = incomeSourceRepository.findAll().size();
        // Create the IncomeSource
        restIncomeSourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(incomeSource)))
            .andExpect(status().isCreated());

        // Validate the IncomeSource in the database
        List<IncomeSource> incomeSourceList = incomeSourceRepository.findAll();
        assertThat(incomeSourceList).hasSize(databaseSizeBeforeCreate + 1);
        IncomeSource testIncomeSource = incomeSourceList.get(incomeSourceList.size() - 1);
        assertThat(testIncomeSource.getIncomeType()).isEqualTo(DEFAULT_INCOME_TYPE);
        assertThat(testIncomeSource.getIncomeAmount()).isEqualTo(DEFAULT_INCOME_AMOUNT);
        assertThat(testIncomeSource.getDuration()).isEqualTo(DEFAULT_DURATION);
    }

    @Test
    @Transactional
    void createIncomeSourceWithExistingId() throws Exception {
        // Create the IncomeSource with an existing ID
        incomeSource.setId(1L);

        int databaseSizeBeforeCreate = incomeSourceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIncomeSourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(incomeSource)))
            .andExpect(status().isBadRequest());

        // Validate the IncomeSource in the database
        List<IncomeSource> incomeSourceList = incomeSourceRepository.findAll();
        assertThat(incomeSourceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIncomeTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = incomeSourceRepository.findAll().size();
        // set the field null
        incomeSource.setIncomeType(null);

        // Create the IncomeSource, which fails.

        restIncomeSourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(incomeSource)))
            .andExpect(status().isBadRequest());

        List<IncomeSource> incomeSourceList = incomeSourceRepository.findAll();
        assertThat(incomeSourceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIncomeAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = incomeSourceRepository.findAll().size();
        // set the field null
        incomeSource.setIncomeAmount(null);

        // Create the IncomeSource, which fails.

        restIncomeSourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(incomeSource)))
            .andExpect(status().isBadRequest());

        List<IncomeSource> incomeSourceList = incomeSourceRepository.findAll();
        assertThat(incomeSourceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = incomeSourceRepository.findAll().size();
        // set the field null
        incomeSource.setDuration(null);

        // Create the IncomeSource, which fails.

        restIncomeSourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(incomeSource)))
            .andExpect(status().isBadRequest());

        List<IncomeSource> incomeSourceList = incomeSourceRepository.findAll();
        assertThat(incomeSourceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIncomeSources() throws Exception {
        // Initialize the database
        incomeSourceRepository.saveAndFlush(incomeSource);

        // Get all the incomeSourceList
        restIncomeSourceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(incomeSource.getId().intValue())))
            .andExpect(jsonPath("$.[*].incomeType").value(hasItem(DEFAULT_INCOME_TYPE.toString())))
            .andExpect(jsonPath("$.[*].incomeAmount").value(hasItem(DEFAULT_INCOME_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.intValue())));
    }

    @Test
    @Transactional
    void getIncomeSource() throws Exception {
        // Initialize the database
        incomeSourceRepository.saveAndFlush(incomeSource);

        // Get the incomeSource
        restIncomeSourceMockMvc
            .perform(get(ENTITY_API_URL_ID, incomeSource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(incomeSource.getId().intValue()))
            .andExpect(jsonPath("$.incomeType").value(DEFAULT_INCOME_TYPE.toString()))
            .andExpect(jsonPath("$.incomeAmount").value(DEFAULT_INCOME_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingIncomeSource() throws Exception {
        // Get the incomeSource
        restIncomeSourceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewIncomeSource() throws Exception {
        // Initialize the database
        incomeSourceRepository.saveAndFlush(incomeSource);

        int databaseSizeBeforeUpdate = incomeSourceRepository.findAll().size();

        // Update the incomeSource
        IncomeSource updatedIncomeSource = incomeSourceRepository.findById(incomeSource.getId()).get();
        // Disconnect from session so that the updates on updatedIncomeSource are not directly saved in db
        em.detach(updatedIncomeSource);
        updatedIncomeSource.incomeType(UPDATED_INCOME_TYPE).incomeAmount(UPDATED_INCOME_AMOUNT).duration(UPDATED_DURATION);

        restIncomeSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIncomeSource.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedIncomeSource))
            )
            .andExpect(status().isOk());

        // Validate the IncomeSource in the database
        List<IncomeSource> incomeSourceList = incomeSourceRepository.findAll();
        assertThat(incomeSourceList).hasSize(databaseSizeBeforeUpdate);
        IncomeSource testIncomeSource = incomeSourceList.get(incomeSourceList.size() - 1);
        assertThat(testIncomeSource.getIncomeType()).isEqualTo(UPDATED_INCOME_TYPE);
        assertThat(testIncomeSource.getIncomeAmount()).isEqualTo(UPDATED_INCOME_AMOUNT);
        assertThat(testIncomeSource.getDuration()).isEqualTo(UPDATED_DURATION);
    }

    @Test
    @Transactional
    void putNonExistingIncomeSource() throws Exception {
        int databaseSizeBeforeUpdate = incomeSourceRepository.findAll().size();
        incomeSource.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIncomeSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, incomeSource.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(incomeSource))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncomeSource in the database
        List<IncomeSource> incomeSourceList = incomeSourceRepository.findAll();
        assertThat(incomeSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIncomeSource() throws Exception {
        int databaseSizeBeforeUpdate = incomeSourceRepository.findAll().size();
        incomeSource.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncomeSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(incomeSource))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncomeSource in the database
        List<IncomeSource> incomeSourceList = incomeSourceRepository.findAll();
        assertThat(incomeSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIncomeSource() throws Exception {
        int databaseSizeBeforeUpdate = incomeSourceRepository.findAll().size();
        incomeSource.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncomeSourceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(incomeSource)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IncomeSource in the database
        List<IncomeSource> incomeSourceList = incomeSourceRepository.findAll();
        assertThat(incomeSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIncomeSourceWithPatch() throws Exception {
        // Initialize the database
        incomeSourceRepository.saveAndFlush(incomeSource);

        int databaseSizeBeforeUpdate = incomeSourceRepository.findAll().size();

        // Update the incomeSource using partial update
        IncomeSource partialUpdatedIncomeSource = new IncomeSource();
        partialUpdatedIncomeSource.setId(incomeSource.getId());

        partialUpdatedIncomeSource.incomeType(UPDATED_INCOME_TYPE).duration(UPDATED_DURATION);

        restIncomeSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIncomeSource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIncomeSource))
            )
            .andExpect(status().isOk());

        // Validate the IncomeSource in the database
        List<IncomeSource> incomeSourceList = incomeSourceRepository.findAll();
        assertThat(incomeSourceList).hasSize(databaseSizeBeforeUpdate);
        IncomeSource testIncomeSource = incomeSourceList.get(incomeSourceList.size() - 1);
        assertThat(testIncomeSource.getIncomeType()).isEqualTo(UPDATED_INCOME_TYPE);
        assertThat(testIncomeSource.getIncomeAmount()).isEqualTo(DEFAULT_INCOME_AMOUNT);
        assertThat(testIncomeSource.getDuration()).isEqualTo(UPDATED_DURATION);
    }

    @Test
    @Transactional
    void fullUpdateIncomeSourceWithPatch() throws Exception {
        // Initialize the database
        incomeSourceRepository.saveAndFlush(incomeSource);

        int databaseSizeBeforeUpdate = incomeSourceRepository.findAll().size();

        // Update the incomeSource using partial update
        IncomeSource partialUpdatedIncomeSource = new IncomeSource();
        partialUpdatedIncomeSource.setId(incomeSource.getId());

        partialUpdatedIncomeSource.incomeType(UPDATED_INCOME_TYPE).incomeAmount(UPDATED_INCOME_AMOUNT).duration(UPDATED_DURATION);

        restIncomeSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIncomeSource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIncomeSource))
            )
            .andExpect(status().isOk());

        // Validate the IncomeSource in the database
        List<IncomeSource> incomeSourceList = incomeSourceRepository.findAll();
        assertThat(incomeSourceList).hasSize(databaseSizeBeforeUpdate);
        IncomeSource testIncomeSource = incomeSourceList.get(incomeSourceList.size() - 1);
        assertThat(testIncomeSource.getIncomeType()).isEqualTo(UPDATED_INCOME_TYPE);
        assertThat(testIncomeSource.getIncomeAmount()).isEqualTo(UPDATED_INCOME_AMOUNT);
        assertThat(testIncomeSource.getDuration()).isEqualTo(UPDATED_DURATION);
    }

    @Test
    @Transactional
    void patchNonExistingIncomeSource() throws Exception {
        int databaseSizeBeforeUpdate = incomeSourceRepository.findAll().size();
        incomeSource.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIncomeSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, incomeSource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(incomeSource))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncomeSource in the database
        List<IncomeSource> incomeSourceList = incomeSourceRepository.findAll();
        assertThat(incomeSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIncomeSource() throws Exception {
        int databaseSizeBeforeUpdate = incomeSourceRepository.findAll().size();
        incomeSource.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncomeSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(incomeSource))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncomeSource in the database
        List<IncomeSource> incomeSourceList = incomeSourceRepository.findAll();
        assertThat(incomeSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIncomeSource() throws Exception {
        int databaseSizeBeforeUpdate = incomeSourceRepository.findAll().size();
        incomeSource.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncomeSourceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(incomeSource))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IncomeSource in the database
        List<IncomeSource> incomeSourceList = incomeSourceRepository.findAll();
        assertThat(incomeSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIncomeSource() throws Exception {
        // Initialize the database
        incomeSourceRepository.saveAndFlush(incomeSource);

        int databaseSizeBeforeDelete = incomeSourceRepository.findAll().size();

        // Delete the incomeSource
        restIncomeSourceMockMvc
            .perform(delete(ENTITY_API_URL_ID, incomeSource.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IncomeSource> incomeSourceList = incomeSourceRepository.findAll();
        assertThat(incomeSourceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
