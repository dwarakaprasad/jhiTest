package com.myapp.service.impl;

import com.myapp.domain.PaymentInfo;
import com.myapp.repository.PaymentInfoRepository;
import com.myapp.service.PaymentInfoService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PaymentInfo}.
 */
@Service
@Transactional
public class PaymentInfoServiceImpl implements PaymentInfoService {

    private final Logger log = LoggerFactory.getLogger(PaymentInfoServiceImpl.class);

    private final PaymentInfoRepository paymentInfoRepository;

    public PaymentInfoServiceImpl(PaymentInfoRepository paymentInfoRepository) {
        this.paymentInfoRepository = paymentInfoRepository;
    }

    @Override
    public PaymentInfo save(PaymentInfo paymentInfo) {
        log.debug("Request to save PaymentInfo : {}", paymentInfo);
        return paymentInfoRepository.save(paymentInfo);
    }

    @Override
    public PaymentInfo update(PaymentInfo paymentInfo) {
        log.debug("Request to save PaymentInfo : {}", paymentInfo);
        return paymentInfoRepository.save(paymentInfo);
    }

    @Override
    public Optional<PaymentInfo> partialUpdate(PaymentInfo paymentInfo) {
        log.debug("Request to partially update PaymentInfo : {}", paymentInfo);

        return paymentInfoRepository
            .findById(paymentInfo.getId())
            .map(existingPaymentInfo -> {
                if (paymentInfo.getPaymentType() != null) {
                    existingPaymentInfo.setPaymentType(paymentInfo.getPaymentType());
                }
                if (paymentInfo.getNumber() != null) {
                    existingPaymentInfo.setNumber(paymentInfo.getNumber());
                }
                if (paymentInfo.getExpiry() != null) {
                    existingPaymentInfo.setExpiry(paymentInfo.getExpiry());
                }
                if (paymentInfo.getSecurity() != null) {
                    existingPaymentInfo.setSecurity(paymentInfo.getSecurity());
                }

                return existingPaymentInfo;
            })
            .map(paymentInfoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentInfo> findAll() {
        log.debug("Request to get all PaymentInfos");
        return paymentInfoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentInfo> findOne(Long id) {
        log.debug("Request to get PaymentInfo : {}", id);
        return paymentInfoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaymentInfo : {}", id);
        paymentInfoRepository.deleteById(id);
    }
}
