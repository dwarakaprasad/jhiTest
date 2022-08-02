package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.Application;
import com.myapp.domain.enumeration.APPLICATIONTYPE;
import com.myapp.repository.ApplicationRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
 * Integration tests for the {@link ApplicationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApplicationResourceIT {

    private static final APPLICATIONTYPE DEFAULT_APPLICATION_TYPE = APPLICATIONTYPE.DL;
    private static final APPLICATIONTYPE UPDATED_APPLICATION_TYPE = APPLICATIONTYPE.PASSPORT;

    private static final LocalDate DEFAULT_INITIATIONDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INITIATIONDATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_SUBMISSION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SUBMISSION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FINALIZATIONDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FINALIZATIONDATE = LocalDate.now(ZoneId.systemDefault());

    private static final UUID DEFAULT_APPLICATION_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_APPLICATION_IDENTIFIER = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/applications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApplicationMockMvc;

    private Application application;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Application createEntity(EntityManager em) {
        Application application = new Application()
            .applicationType(DEFAULT_APPLICATION_TYPE)
            .initiationdate(DEFAULT_INITIATIONDATE)
            .submissionDate(DEFAULT_SUBMISSION_DATE)
            .finalizationdate(DEFAULT_FINALIZATIONDATE)
            .applicationIdentifier(DEFAULT_APPLICATION_IDENTIFIER);
        return application;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Application createUpdatedEntity(EntityManager em) {
        Application application = new Application()
            .applicationType(UPDATED_APPLICATION_TYPE)
            .initiationdate(UPDATED_INITIATIONDATE)
            .submissionDate(UPDATED_SUBMISSION_DATE)
            .finalizationdate(UPDATED_FINALIZATIONDATE)
            .applicationIdentifier(UPDATED_APPLICATION_IDENTIFIER);
        return application;
    }

    @BeforeEach
    public void initTest() {
        application = createEntity(em);
    }

    @Test
    @Transactional
    void createApplication() throws Exception {
        int databaseSizeBeforeCreate = applicationRepository.findAll().size();
        // Create the Application
        restApplicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(application)))
            .andExpect(status().isCreated());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeCreate + 1);
        Application testApplication = applicationList.get(applicationList.size() - 1);
        assertThat(testApplication.getApplicationType()).isEqualTo(DEFAULT_APPLICATION_TYPE);
        assertThat(testApplication.getInitiationdate()).isEqualTo(DEFAULT_INITIATIONDATE);
        assertThat(testApplication.getSubmissionDate()).isEqualTo(DEFAULT_SUBMISSION_DATE);
        assertThat(testApplication.getFinalizationdate()).isEqualTo(DEFAULT_FINALIZATIONDATE);
        assertThat(testApplication.getApplicationIdentifier()).isEqualTo(DEFAULT_APPLICATION_IDENTIFIER);
    }

    @Test
    @Transactional
    void createApplicationWithExistingId() throws Exception {
        // Create the Application with an existing ID
        application.setId(1L);

        int databaseSizeBeforeCreate = applicationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(application)))
            .andExpect(status().isBadRequest());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkApplicationTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationRepository.findAll().size();
        // set the field null
        application.setApplicationType(null);

        // Create the Application, which fails.

        restApplicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(application)))
            .andExpect(status().isBadRequest());

        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInitiationdateIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationRepository.findAll().size();
        // set the field null
        application.setInitiationdate(null);

        // Create the Application, which fails.

        restApplicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(application)))
            .andExpect(status().isBadRequest());

        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubmissionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationRepository.findAll().size();
        // set the field null
        application.setSubmissionDate(null);

        // Create the Application, which fails.

        restApplicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(application)))
            .andExpect(status().isBadRequest());

        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFinalizationdateIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationRepository.findAll().size();
        // set the field null
        application.setFinalizationdate(null);

        // Create the Application, which fails.

        restApplicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(application)))
            .andExpect(status().isBadRequest());

        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApplications() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList
        restApplicationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(application.getId().intValue())))
            .andExpect(jsonPath("$.[*].applicationType").value(hasItem(DEFAULT_APPLICATION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].initiationdate").value(hasItem(DEFAULT_INITIATIONDATE.toString())))
            .andExpect(jsonPath("$.[*].submissionDate").value(hasItem(DEFAULT_SUBMISSION_DATE.toString())))
            .andExpect(jsonPath("$.[*].finalizationdate").value(hasItem(DEFAULT_FINALIZATIONDATE.toString())))
            .andExpect(jsonPath("$.[*].applicationIdentifier").value(hasItem(DEFAULT_APPLICATION_IDENTIFIER.toString())));
    }

    @Test
    @Transactional
    void getApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get the application
        restApplicationMockMvc
            .perform(get(ENTITY_API_URL_ID, application.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(application.getId().intValue()))
            .andExpect(jsonPath("$.applicationType").value(DEFAULT_APPLICATION_TYPE.toString()))
            .andExpect(jsonPath("$.initiationdate").value(DEFAULT_INITIATIONDATE.toString()))
            .andExpect(jsonPath("$.submissionDate").value(DEFAULT_SUBMISSION_DATE.toString()))
            .andExpect(jsonPath("$.finalizationdate").value(DEFAULT_FINALIZATIONDATE.toString()))
            .andExpect(jsonPath("$.applicationIdentifier").value(DEFAULT_APPLICATION_IDENTIFIER.toString()));
    }

    @Test
    @Transactional
    void getNonExistingApplication() throws Exception {
        // Get the application
        restApplicationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();

        // Update the application
        Application updatedApplication = applicationRepository.findById(application.getId()).get();
        // Disconnect from session so that the updates on updatedApplication are not directly saved in db
        em.detach(updatedApplication);
        updatedApplication
            .applicationType(UPDATED_APPLICATION_TYPE)
            .initiationdate(UPDATED_INITIATIONDATE)
            .submissionDate(UPDATED_SUBMISSION_DATE)
            .finalizationdate(UPDATED_FINALIZATIONDATE)
            .applicationIdentifier(UPDATED_APPLICATION_IDENTIFIER);

        restApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApplication.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApplication))
            )
            .andExpect(status().isOk());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
        Application testApplication = applicationList.get(applicationList.size() - 1);
        assertThat(testApplication.getApplicationType()).isEqualTo(UPDATED_APPLICATION_TYPE);
        assertThat(testApplication.getInitiationdate()).isEqualTo(UPDATED_INITIATIONDATE);
        assertThat(testApplication.getSubmissionDate()).isEqualTo(UPDATED_SUBMISSION_DATE);
        assertThat(testApplication.getFinalizationdate()).isEqualTo(UPDATED_FINALIZATIONDATE);
        assertThat(testApplication.getApplicationIdentifier()).isEqualTo(UPDATED_APPLICATION_IDENTIFIER);
    }

    @Test
    @Transactional
    void putNonExistingApplication() throws Exception {
        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();
        application.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, application.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(application))
            )
            .andExpect(status().isBadRequest());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApplication() throws Exception {
        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();
        application.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(application))
            )
            .andExpect(status().isBadRequest());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApplication() throws Exception {
        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();
        application.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(application)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApplicationWithPatch() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();

        // Update the application using partial update
        Application partialUpdatedApplication = new Application();
        partialUpdatedApplication.setId(application.getId());

        partialUpdatedApplication.initiationdate(UPDATED_INITIATIONDATE);

        restApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplication.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplication))
            )
            .andExpect(status().isOk());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
        Application testApplication = applicationList.get(applicationList.size() - 1);
        assertThat(testApplication.getApplicationType()).isEqualTo(DEFAULT_APPLICATION_TYPE);
        assertThat(testApplication.getInitiationdate()).isEqualTo(UPDATED_INITIATIONDATE);
        assertThat(testApplication.getSubmissionDate()).isEqualTo(DEFAULT_SUBMISSION_DATE);
        assertThat(testApplication.getFinalizationdate()).isEqualTo(DEFAULT_FINALIZATIONDATE);
        assertThat(testApplication.getApplicationIdentifier()).isEqualTo(DEFAULT_APPLICATION_IDENTIFIER);
    }

    @Test
    @Transactional
    void fullUpdateApplicationWithPatch() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();

        // Update the application using partial update
        Application partialUpdatedApplication = new Application();
        partialUpdatedApplication.setId(application.getId());

        partialUpdatedApplication
            .applicationType(UPDATED_APPLICATION_TYPE)
            .initiationdate(UPDATED_INITIATIONDATE)
            .submissionDate(UPDATED_SUBMISSION_DATE)
            .finalizationdate(UPDATED_FINALIZATIONDATE)
            .applicationIdentifier(UPDATED_APPLICATION_IDENTIFIER);

        restApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplication.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplication))
            )
            .andExpect(status().isOk());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
        Application testApplication = applicationList.get(applicationList.size() - 1);
        assertThat(testApplication.getApplicationType()).isEqualTo(UPDATED_APPLICATION_TYPE);
        assertThat(testApplication.getInitiationdate()).isEqualTo(UPDATED_INITIATIONDATE);
        assertThat(testApplication.getSubmissionDate()).isEqualTo(UPDATED_SUBMISSION_DATE);
        assertThat(testApplication.getFinalizationdate()).isEqualTo(UPDATED_FINALIZATIONDATE);
        assertThat(testApplication.getApplicationIdentifier()).isEqualTo(UPDATED_APPLICATION_IDENTIFIER);
    }

    @Test
    @Transactional
    void patchNonExistingApplication() throws Exception {
        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();
        application.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, application.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(application))
            )
            .andExpect(status().isBadRequest());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApplication() throws Exception {
        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();
        application.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(application))
            )
            .andExpect(status().isBadRequest());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApplication() throws Exception {
        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();
        application.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(application))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        int databaseSizeBeforeDelete = applicationRepository.findAll().size();

        // Delete the application
        restApplicationMockMvc
            .perform(delete(ENTITY_API_URL_ID, application.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
