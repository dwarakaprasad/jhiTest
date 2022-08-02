package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.Passport;
import com.myapp.domain.enumeration.PASSPORTTYPE;
import com.myapp.repository.PassportRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PassportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PassportResourceIT {

    private static final String DEFAULT_IDENTITY = "AAAAAAAAAA";
    private static final String UPDATED_IDENTITY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_EXPIRY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRY = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_ISSUING_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_ISSUING_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_NUMBER = "BBBBBBBBBB";

    private static final PASSPORTTYPE DEFAULT_PASSPORT_TYPE = PASSPORTTYPE.DIPLOMATIC;
    private static final PASSPORTTYPE UPDATED_PASSPORT_TYPE = PASSPORTTYPE.REGULAR;

    private static final String ENTITY_API_URL = "/api/passports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PassportRepository passportRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPassportMockMvc;

    private Passport passport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Passport createEntity(EntityManager em) {
        Passport passport = new Passport()
            .identity(DEFAULT_IDENTITY)
            .expiry(DEFAULT_EXPIRY)
            .issuingCountry(DEFAULT_ISSUING_COUNTRY)
            .documentNumber(DEFAULT_DOCUMENT_NUMBER)
            .passportType(DEFAULT_PASSPORT_TYPE);
        return passport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Passport createUpdatedEntity(EntityManager em) {
        Passport passport = new Passport()
            .identity(UPDATED_IDENTITY)
            .expiry(UPDATED_EXPIRY)
            .issuingCountry(UPDATED_ISSUING_COUNTRY)
            .documentNumber(UPDATED_DOCUMENT_NUMBER)
            .passportType(UPDATED_PASSPORT_TYPE);
        return passport;
    }

    @BeforeEach
    public void initTest() {
        passport = createEntity(em);
    }

    @Test
    @Transactional
    void createPassport() throws Exception {
        int databaseSizeBeforeCreate = passportRepository.findAll().size();
        // Create the Passport
        restPassportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(passport)))
            .andExpect(status().isCreated());

        // Validate the Passport in the database
        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeCreate + 1);
        Passport testPassport = passportList.get(passportList.size() - 1);
        assertThat(testPassport.getIdentity()).isEqualTo(DEFAULT_IDENTITY);
        assertThat(testPassport.getExpiry()).isEqualTo(DEFAULT_EXPIRY);
        assertThat(testPassport.getIssuingCountry()).isEqualTo(DEFAULT_ISSUING_COUNTRY);
        assertThat(testPassport.getDocumentNumber()).isEqualTo(DEFAULT_DOCUMENT_NUMBER);
        assertThat(testPassport.getPassportType()).isEqualTo(DEFAULT_PASSPORT_TYPE);
    }

    @Test
    @Transactional
    void createPassportWithExistingId() throws Exception {
        // Create the Passport with an existing ID
        passport.setId(1L);

        int databaseSizeBeforeCreate = passportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPassportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(passport)))
            .andExpect(status().isBadRequest());

        // Validate the Passport in the database
        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdentityIsRequired() throws Exception {
        int databaseSizeBeforeTest = passportRepository.findAll().size();
        // set the field null
        passport.setIdentity(null);

        // Create the Passport, which fails.

        restPassportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(passport)))
            .andExpect(status().isBadRequest());

        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExpiryIsRequired() throws Exception {
        int databaseSizeBeforeTest = passportRepository.findAll().size();
        // set the field null
        passport.setExpiry(null);

        // Create the Passport, which fails.

        restPassportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(passport)))
            .andExpect(status().isBadRequest());

        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIssuingCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = passportRepository.findAll().size();
        // set the field null
        passport.setIssuingCountry(null);

        // Create the Passport, which fails.

        restPassportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(passport)))
            .andExpect(status().isBadRequest());

        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPassportTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = passportRepository.findAll().size();
        // set the field null
        passport.setPassportType(null);

        // Create the Passport, which fails.

        restPassportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(passport)))
            .andExpect(status().isBadRequest());

        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPassports() throws Exception {
        // Initialize the database
        passportRepository.saveAndFlush(passport);

        // Get all the passportList
        restPassportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(passport.getId().intValue())))
            .andExpect(jsonPath("$.[*].identity").value(hasItem(DEFAULT_IDENTITY)))
            .andExpect(jsonPath("$.[*].expiry").value(hasItem(DEFAULT_EXPIRY.toString())))
            .andExpect(jsonPath("$.[*].issuingCountry").value(hasItem(DEFAULT_ISSUING_COUNTRY)))
            .andExpect(jsonPath("$.[*].documentNumber").value(hasItem(DEFAULT_DOCUMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].passportType").value(hasItem(DEFAULT_PASSPORT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getPassport() throws Exception {
        // Initialize the database
        passportRepository.saveAndFlush(passport);

        // Get the passport
        restPassportMockMvc
            .perform(get(ENTITY_API_URL_ID, passport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(passport.getId().intValue()))
            .andExpect(jsonPath("$.identity").value(DEFAULT_IDENTITY))
            .andExpect(jsonPath("$.expiry").value(DEFAULT_EXPIRY.toString()))
            .andExpect(jsonPath("$.issuingCountry").value(DEFAULT_ISSUING_COUNTRY))
            .andExpect(jsonPath("$.documentNumber").value(DEFAULT_DOCUMENT_NUMBER))
            .andExpect(jsonPath("$.passportType").value(DEFAULT_PASSPORT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPassport() throws Exception {
        // Get the passport
        restPassportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPassport() throws Exception {
        // Initialize the database
        passportRepository.saveAndFlush(passport);

        int databaseSizeBeforeUpdate = passportRepository.findAll().size();

        // Update the passport
        Passport updatedPassport = passportRepository.findById(passport.getId()).get();
        // Disconnect from session so that the updates on updatedPassport are not directly saved in db
        em.detach(updatedPassport);
        updatedPassport
            .identity(UPDATED_IDENTITY)
            .expiry(UPDATED_EXPIRY)
            .issuingCountry(UPDATED_ISSUING_COUNTRY)
            .documentNumber(UPDATED_DOCUMENT_NUMBER)
            .passportType(UPDATED_PASSPORT_TYPE);

        restPassportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPassport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPassport))
            )
            .andExpect(status().isOk());

        // Validate the Passport in the database
        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeUpdate);
        Passport testPassport = passportList.get(passportList.size() - 1);
        assertThat(testPassport.getIdentity()).isEqualTo(UPDATED_IDENTITY);
        assertThat(testPassport.getExpiry()).isEqualTo(UPDATED_EXPIRY);
        assertThat(testPassport.getIssuingCountry()).isEqualTo(UPDATED_ISSUING_COUNTRY);
        assertThat(testPassport.getDocumentNumber()).isEqualTo(UPDATED_DOCUMENT_NUMBER);
        assertThat(testPassport.getPassportType()).isEqualTo(UPDATED_PASSPORT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingPassport() throws Exception {
        int databaseSizeBeforeUpdate = passportRepository.findAll().size();
        passport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPassportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, passport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(passport))
            )
            .andExpect(status().isBadRequest());

        // Validate the Passport in the database
        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPassport() throws Exception {
        int databaseSizeBeforeUpdate = passportRepository.findAll().size();
        passport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPassportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(passport))
            )
            .andExpect(status().isBadRequest());

        // Validate the Passport in the database
        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPassport() throws Exception {
        int databaseSizeBeforeUpdate = passportRepository.findAll().size();
        passport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPassportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(passport)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Passport in the database
        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePassportWithPatch() throws Exception {
        // Initialize the database
        passportRepository.saveAndFlush(passport);

        int databaseSizeBeforeUpdate = passportRepository.findAll().size();

        // Update the passport using partial update
        Passport partialUpdatedPassport = new Passport();
        partialUpdatedPassport.setId(passport.getId());

        partialUpdatedPassport.documentNumber(UPDATED_DOCUMENT_NUMBER);

        restPassportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPassport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPassport))
            )
            .andExpect(status().isOk());

        // Validate the Passport in the database
        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeUpdate);
        Passport testPassport = passportList.get(passportList.size() - 1);
        assertThat(testPassport.getIdentity()).isEqualTo(DEFAULT_IDENTITY);
        assertThat(testPassport.getExpiry()).isEqualTo(DEFAULT_EXPIRY);
        assertThat(testPassport.getIssuingCountry()).isEqualTo(DEFAULT_ISSUING_COUNTRY);
        assertThat(testPassport.getDocumentNumber()).isEqualTo(UPDATED_DOCUMENT_NUMBER);
        assertThat(testPassport.getPassportType()).isEqualTo(DEFAULT_PASSPORT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdatePassportWithPatch() throws Exception {
        // Initialize the database
        passportRepository.saveAndFlush(passport);

        int databaseSizeBeforeUpdate = passportRepository.findAll().size();

        // Update the passport using partial update
        Passport partialUpdatedPassport = new Passport();
        partialUpdatedPassport.setId(passport.getId());

        partialUpdatedPassport
            .identity(UPDATED_IDENTITY)
            .expiry(UPDATED_EXPIRY)
            .issuingCountry(UPDATED_ISSUING_COUNTRY)
            .documentNumber(UPDATED_DOCUMENT_NUMBER)
            .passportType(UPDATED_PASSPORT_TYPE);

        restPassportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPassport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPassport))
            )
            .andExpect(status().isOk());

        // Validate the Passport in the database
        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeUpdate);
        Passport testPassport = passportList.get(passportList.size() - 1);
        assertThat(testPassport.getIdentity()).isEqualTo(UPDATED_IDENTITY);
        assertThat(testPassport.getExpiry()).isEqualTo(UPDATED_EXPIRY);
        assertThat(testPassport.getIssuingCountry()).isEqualTo(UPDATED_ISSUING_COUNTRY);
        assertThat(testPassport.getDocumentNumber()).isEqualTo(UPDATED_DOCUMENT_NUMBER);
        assertThat(testPassport.getPassportType()).isEqualTo(UPDATED_PASSPORT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingPassport() throws Exception {
        int databaseSizeBeforeUpdate = passportRepository.findAll().size();
        passport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPassportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, passport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(passport))
            )
            .andExpect(status().isBadRequest());

        // Validate the Passport in the database
        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPassport() throws Exception {
        int databaseSizeBeforeUpdate = passportRepository.findAll().size();
        passport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPassportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(passport))
            )
            .andExpect(status().isBadRequest());

        // Validate the Passport in the database
        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPassport() throws Exception {
        int databaseSizeBeforeUpdate = passportRepository.findAll().size();
        passport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPassportMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(passport)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Passport in the database
        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePassport() throws Exception {
        // Initialize the database
        passportRepository.saveAndFlush(passport);

        int databaseSizeBeforeDelete = passportRepository.findAll().size();

        // Delete the passport
        restPassportMockMvc
            .perform(delete(ENTITY_API_URL_ID, passport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
