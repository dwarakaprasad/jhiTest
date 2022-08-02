package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.Applicant;
import com.myapp.domain.enumeration.GENDER;
import com.myapp.repository.ApplicantRepository;
import com.myapp.service.ApplicantService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ApplicantResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ApplicantResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MIDDLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MIDDLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DOB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DOB = LocalDate.now(ZoneId.systemDefault());

    private static final GENDER DEFAULT_GENDER = GENDER.MALE;
    private static final GENDER UPDATED_GENDER = GENDER.FEMALE;

    private static final String ENTITY_API_URL = "/api/applicants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApplicantRepository applicantRepository;

    @Mock
    private ApplicantRepository applicantRepositoryMock;

    @Mock
    private ApplicantService applicantServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApplicantMockMvc;

    private Applicant applicant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Applicant createEntity(EntityManager em) {
        Applicant applicant = new Applicant()
            .firstName(DEFAULT_FIRST_NAME)
            .middleName(DEFAULT_MIDDLE_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .dob(DEFAULT_DOB)
            .gender(DEFAULT_GENDER);
        return applicant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Applicant createUpdatedEntity(EntityManager em) {
        Applicant applicant = new Applicant()
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .dob(UPDATED_DOB)
            .gender(UPDATED_GENDER);
        return applicant;
    }

    @BeforeEach
    public void initTest() {
        applicant = createEntity(em);
    }

    @Test
    @Transactional
    void createApplicant() throws Exception {
        int databaseSizeBeforeCreate = applicantRepository.findAll().size();
        // Create the Applicant
        restApplicantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicant)))
            .andExpect(status().isCreated());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeCreate + 1);
        Applicant testApplicant = applicantList.get(applicantList.size() - 1);
        assertThat(testApplicant.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testApplicant.getMiddleName()).isEqualTo(DEFAULT_MIDDLE_NAME);
        assertThat(testApplicant.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testApplicant.getDob()).isEqualTo(DEFAULT_DOB);
        assertThat(testApplicant.getGender()).isEqualTo(DEFAULT_GENDER);
    }

    @Test
    @Transactional
    void createApplicantWithExistingId() throws Exception {
        // Create the Applicant with an existing ID
        applicant.setId(1L);

        int databaseSizeBeforeCreate = applicantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicant)))
            .andExpect(status().isBadRequest());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicantRepository.findAll().size();
        // set the field null
        applicant.setFirstName(null);

        // Create the Applicant, which fails.

        restApplicantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicant)))
            .andExpect(status().isBadRequest());

        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicantRepository.findAll().size();
        // set the field null
        applicant.setLastName(null);

        // Create the Applicant, which fails.

        restApplicantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicant)))
            .andExpect(status().isBadRequest());

        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDobIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicantRepository.findAll().size();
        // set the field null
        applicant.setDob(null);

        // Create the Applicant, which fails.

        restApplicantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicant)))
            .andExpect(status().isBadRequest());

        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicantRepository.findAll().size();
        // set the field null
        applicant.setGender(null);

        // Create the Applicant, which fails.

        restApplicantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicant)))
            .andExpect(status().isBadRequest());

        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApplicants() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        // Get all the applicantList
        restApplicantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicant.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllApplicantsWithEagerRelationshipsIsEnabled() throws Exception {
        when(applicantServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restApplicantMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(applicantServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllApplicantsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(applicantServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restApplicantMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(applicantServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getApplicant() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        // Get the applicant
        restApplicantMockMvc
            .perform(get(ENTITY_API_URL_ID, applicant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(applicant.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.middleName").value(DEFAULT_MIDDLE_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()));
    }

    @Test
    @Transactional
    void getNonExistingApplicant() throws Exception {
        // Get the applicant
        restApplicantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewApplicant() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        int databaseSizeBeforeUpdate = applicantRepository.findAll().size();

        // Update the applicant
        Applicant updatedApplicant = applicantRepository.findById(applicant.getId()).get();
        // Disconnect from session so that the updates on updatedApplicant are not directly saved in db
        em.detach(updatedApplicant);
        updatedApplicant
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .dob(UPDATED_DOB)
            .gender(UPDATED_GENDER);

        restApplicantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApplicant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApplicant))
            )
            .andExpect(status().isOk());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeUpdate);
        Applicant testApplicant = applicantList.get(applicantList.size() - 1);
        assertThat(testApplicant.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testApplicant.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testApplicant.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testApplicant.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testApplicant.getGender()).isEqualTo(UPDATED_GENDER);
    }

    @Test
    @Transactional
    void putNonExistingApplicant() throws Exception {
        int databaseSizeBeforeUpdate = applicantRepository.findAll().size();
        applicant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, applicant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApplicant() throws Exception {
        int databaseSizeBeforeUpdate = applicantRepository.findAll().size();
        applicant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApplicant() throws Exception {
        int databaseSizeBeforeUpdate = applicantRepository.findAll().size();
        applicant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicantMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicant)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApplicantWithPatch() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        int databaseSizeBeforeUpdate = applicantRepository.findAll().size();

        // Update the applicant using partial update
        Applicant partialUpdatedApplicant = new Applicant();
        partialUpdatedApplicant.setId(applicant.getId());

        partialUpdatedApplicant.middleName(UPDATED_MIDDLE_NAME).dob(UPDATED_DOB).gender(UPDATED_GENDER);

        restApplicantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplicant))
            )
            .andExpect(status().isOk());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeUpdate);
        Applicant testApplicant = applicantList.get(applicantList.size() - 1);
        assertThat(testApplicant.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testApplicant.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testApplicant.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testApplicant.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testApplicant.getGender()).isEqualTo(UPDATED_GENDER);
    }

    @Test
    @Transactional
    void fullUpdateApplicantWithPatch() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        int databaseSizeBeforeUpdate = applicantRepository.findAll().size();

        // Update the applicant using partial update
        Applicant partialUpdatedApplicant = new Applicant();
        partialUpdatedApplicant.setId(applicant.getId());

        partialUpdatedApplicant
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .dob(UPDATED_DOB)
            .gender(UPDATED_GENDER);

        restApplicantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplicant))
            )
            .andExpect(status().isOk());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeUpdate);
        Applicant testApplicant = applicantList.get(applicantList.size() - 1);
        assertThat(testApplicant.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testApplicant.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testApplicant.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testApplicant.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testApplicant.getGender()).isEqualTo(UPDATED_GENDER);
    }

    @Test
    @Transactional
    void patchNonExistingApplicant() throws Exception {
        int databaseSizeBeforeUpdate = applicantRepository.findAll().size();
        applicant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, applicant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApplicant() throws Exception {
        int databaseSizeBeforeUpdate = applicantRepository.findAll().size();
        applicant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApplicant() throws Exception {
        int databaseSizeBeforeUpdate = applicantRepository.findAll().size();
        applicant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicantMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(applicant))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApplicant() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        int databaseSizeBeforeDelete = applicantRepository.findAll().size();

        // Delete the applicant
        restApplicantMockMvc
            .perform(delete(ENTITY_API_URL_ID, applicant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
