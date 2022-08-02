package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.PaymentInfo;
import com.myapp.domain.enumeration.PAYMENTTYPE;
import com.myapp.repository.PaymentInfoRepository;
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
 * Integration tests for the {@link PaymentInfoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentInfoResourceIT {

    private static final PAYMENTTYPE DEFAULT_PAYMENT_TYPE = PAYMENTTYPE.CREDIT;
    private static final PAYMENTTYPE UPDATED_PAYMENT_TYPE = PAYMENTTYPE.DEBIT;

    private static final Long DEFAULT_NUMBER = 1L;
    private static final Long UPDATED_NUMBER = 2L;

    private static final LocalDate DEFAULT_EXPIRY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRY = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_SECURITY = 1L;
    private static final Long UPDATED_SECURITY = 2L;

    private static final String ENTITY_API_URL = "/api/payment-infos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaymentInfoRepository paymentInfoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentInfoMockMvc;

    private PaymentInfo paymentInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentInfo createEntity(EntityManager em) {
        PaymentInfo paymentInfo = new PaymentInfo()
            .paymentType(DEFAULT_PAYMENT_TYPE)
            .number(DEFAULT_NUMBER)
            .expiry(DEFAULT_EXPIRY)
            .security(DEFAULT_SECURITY);
        return paymentInfo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentInfo createUpdatedEntity(EntityManager em) {
        PaymentInfo paymentInfo = new PaymentInfo()
            .paymentType(UPDATED_PAYMENT_TYPE)
            .number(UPDATED_NUMBER)
            .expiry(UPDATED_EXPIRY)
            .security(UPDATED_SECURITY);
        return paymentInfo;
    }

    @BeforeEach
    public void initTest() {
        paymentInfo = createEntity(em);
    }

    @Test
    @Transactional
    void createPaymentInfo() throws Exception {
        int databaseSizeBeforeCreate = paymentInfoRepository.findAll().size();
        // Create the PaymentInfo
        restPaymentInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentInfo)))
            .andExpect(status().isCreated());

        // Validate the PaymentInfo in the database
        List<PaymentInfo> paymentInfoList = paymentInfoRepository.findAll();
        assertThat(paymentInfoList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentInfo testPaymentInfo = paymentInfoList.get(paymentInfoList.size() - 1);
        assertThat(testPaymentInfo.getPaymentType()).isEqualTo(DEFAULT_PAYMENT_TYPE);
        assertThat(testPaymentInfo.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testPaymentInfo.getExpiry()).isEqualTo(DEFAULT_EXPIRY);
        assertThat(testPaymentInfo.getSecurity()).isEqualTo(DEFAULT_SECURITY);
    }

    @Test
    @Transactional
    void createPaymentInfoWithExistingId() throws Exception {
        // Create the PaymentInfo with an existing ID
        paymentInfo.setId(1L);

        int databaseSizeBeforeCreate = paymentInfoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentInfo)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentInfo in the database
        List<PaymentInfo> paymentInfoList = paymentInfoRepository.findAll();
        assertThat(paymentInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPaymentTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentInfoRepository.findAll().size();
        // set the field null
        paymentInfo.setPaymentType(null);

        // Create the PaymentInfo, which fails.

        restPaymentInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentInfo)))
            .andExpect(status().isBadRequest());

        List<PaymentInfo> paymentInfoList = paymentInfoRepository.findAll();
        assertThat(paymentInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentInfoRepository.findAll().size();
        // set the field null
        paymentInfo.setNumber(null);

        // Create the PaymentInfo, which fails.

        restPaymentInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentInfo)))
            .andExpect(status().isBadRequest());

        List<PaymentInfo> paymentInfoList = paymentInfoRepository.findAll();
        assertThat(paymentInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaymentInfos() throws Exception {
        // Initialize the database
        paymentInfoRepository.saveAndFlush(paymentInfo);

        // Get all the paymentInfoList
        restPaymentInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentType").value(hasItem(DEFAULT_PAYMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].expiry").value(hasItem(DEFAULT_EXPIRY.toString())))
            .andExpect(jsonPath("$.[*].security").value(hasItem(DEFAULT_SECURITY.intValue())));
    }

    @Test
    @Transactional
    void getPaymentInfo() throws Exception {
        // Initialize the database
        paymentInfoRepository.saveAndFlush(paymentInfo);

        // Get the paymentInfo
        restPaymentInfoMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentInfo.getId().intValue()))
            .andExpect(jsonPath("$.paymentType").value(DEFAULT_PAYMENT_TYPE.toString()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.intValue()))
            .andExpect(jsonPath("$.expiry").value(DEFAULT_EXPIRY.toString()))
            .andExpect(jsonPath("$.security").value(DEFAULT_SECURITY.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingPaymentInfo() throws Exception {
        // Get the paymentInfo
        restPaymentInfoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPaymentInfo() throws Exception {
        // Initialize the database
        paymentInfoRepository.saveAndFlush(paymentInfo);

        int databaseSizeBeforeUpdate = paymentInfoRepository.findAll().size();

        // Update the paymentInfo
        PaymentInfo updatedPaymentInfo = paymentInfoRepository.findById(paymentInfo.getId()).get();
        // Disconnect from session so that the updates on updatedPaymentInfo are not directly saved in db
        em.detach(updatedPaymentInfo);
        updatedPaymentInfo.paymentType(UPDATED_PAYMENT_TYPE).number(UPDATED_NUMBER).expiry(UPDATED_EXPIRY).security(UPDATED_SECURITY);

        restPaymentInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPaymentInfo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPaymentInfo))
            )
            .andExpect(status().isOk());

        // Validate the PaymentInfo in the database
        List<PaymentInfo> paymentInfoList = paymentInfoRepository.findAll();
        assertThat(paymentInfoList).hasSize(databaseSizeBeforeUpdate);
        PaymentInfo testPaymentInfo = paymentInfoList.get(paymentInfoList.size() - 1);
        assertThat(testPaymentInfo.getPaymentType()).isEqualTo(UPDATED_PAYMENT_TYPE);
        assertThat(testPaymentInfo.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testPaymentInfo.getExpiry()).isEqualTo(UPDATED_EXPIRY);
        assertThat(testPaymentInfo.getSecurity()).isEqualTo(UPDATED_SECURITY);
    }

    @Test
    @Transactional
    void putNonExistingPaymentInfo() throws Exception {
        int databaseSizeBeforeUpdate = paymentInfoRepository.findAll().size();
        paymentInfo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentInfo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentInfo in the database
        List<PaymentInfo> paymentInfoList = paymentInfoRepository.findAll();
        assertThat(paymentInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentInfo() throws Exception {
        int databaseSizeBeforeUpdate = paymentInfoRepository.findAll().size();
        paymentInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentInfo in the database
        List<PaymentInfo> paymentInfoList = paymentInfoRepository.findAll();
        assertThat(paymentInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentInfo() throws Exception {
        int databaseSizeBeforeUpdate = paymentInfoRepository.findAll().size();
        paymentInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentInfoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentInfo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentInfo in the database
        List<PaymentInfo> paymentInfoList = paymentInfoRepository.findAll();
        assertThat(paymentInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentInfoWithPatch() throws Exception {
        // Initialize the database
        paymentInfoRepository.saveAndFlush(paymentInfo);

        int databaseSizeBeforeUpdate = paymentInfoRepository.findAll().size();

        // Update the paymentInfo using partial update
        PaymentInfo partialUpdatedPaymentInfo = new PaymentInfo();
        partialUpdatedPaymentInfo.setId(paymentInfo.getId());

        partialUpdatedPaymentInfo.paymentType(UPDATED_PAYMENT_TYPE).number(UPDATED_NUMBER).security(UPDATED_SECURITY);

        restPaymentInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentInfo))
            )
            .andExpect(status().isOk());

        // Validate the PaymentInfo in the database
        List<PaymentInfo> paymentInfoList = paymentInfoRepository.findAll();
        assertThat(paymentInfoList).hasSize(databaseSizeBeforeUpdate);
        PaymentInfo testPaymentInfo = paymentInfoList.get(paymentInfoList.size() - 1);
        assertThat(testPaymentInfo.getPaymentType()).isEqualTo(UPDATED_PAYMENT_TYPE);
        assertThat(testPaymentInfo.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testPaymentInfo.getExpiry()).isEqualTo(DEFAULT_EXPIRY);
        assertThat(testPaymentInfo.getSecurity()).isEqualTo(UPDATED_SECURITY);
    }

    @Test
    @Transactional
    void fullUpdatePaymentInfoWithPatch() throws Exception {
        // Initialize the database
        paymentInfoRepository.saveAndFlush(paymentInfo);

        int databaseSizeBeforeUpdate = paymentInfoRepository.findAll().size();

        // Update the paymentInfo using partial update
        PaymentInfo partialUpdatedPaymentInfo = new PaymentInfo();
        partialUpdatedPaymentInfo.setId(paymentInfo.getId());

        partialUpdatedPaymentInfo
            .paymentType(UPDATED_PAYMENT_TYPE)
            .number(UPDATED_NUMBER)
            .expiry(UPDATED_EXPIRY)
            .security(UPDATED_SECURITY);

        restPaymentInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentInfo))
            )
            .andExpect(status().isOk());

        // Validate the PaymentInfo in the database
        List<PaymentInfo> paymentInfoList = paymentInfoRepository.findAll();
        assertThat(paymentInfoList).hasSize(databaseSizeBeforeUpdate);
        PaymentInfo testPaymentInfo = paymentInfoList.get(paymentInfoList.size() - 1);
        assertThat(testPaymentInfo.getPaymentType()).isEqualTo(UPDATED_PAYMENT_TYPE);
        assertThat(testPaymentInfo.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testPaymentInfo.getExpiry()).isEqualTo(UPDATED_EXPIRY);
        assertThat(testPaymentInfo.getSecurity()).isEqualTo(UPDATED_SECURITY);
    }

    @Test
    @Transactional
    void patchNonExistingPaymentInfo() throws Exception {
        int databaseSizeBeforeUpdate = paymentInfoRepository.findAll().size();
        paymentInfo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentInfo in the database
        List<PaymentInfo> paymentInfoList = paymentInfoRepository.findAll();
        assertThat(paymentInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentInfo() throws Exception {
        int databaseSizeBeforeUpdate = paymentInfoRepository.findAll().size();
        paymentInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentInfo in the database
        List<PaymentInfo> paymentInfoList = paymentInfoRepository.findAll();
        assertThat(paymentInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentInfo() throws Exception {
        int databaseSizeBeforeUpdate = paymentInfoRepository.findAll().size();
        paymentInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentInfoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(paymentInfo))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentInfo in the database
        List<PaymentInfo> paymentInfoList = paymentInfoRepository.findAll();
        assertThat(paymentInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentInfo() throws Exception {
        // Initialize the database
        paymentInfoRepository.saveAndFlush(paymentInfo);

        int databaseSizeBeforeDelete = paymentInfoRepository.findAll().size();

        // Delete the paymentInfo
        restPaymentInfoMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentInfo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaymentInfo> paymentInfoList = paymentInfoRepository.findAll();
        assertThat(paymentInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
