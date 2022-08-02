package com.myapp.service.impl;

import com.myapp.domain.Passport;
import com.myapp.repository.PassportRepository;
import com.myapp.service.PassportService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Passport}.
 */
@Service
@Transactional
public class PassportServiceImpl implements PassportService {

    private final Logger log = LoggerFactory.getLogger(PassportServiceImpl.class);

    private final PassportRepository passportRepository;

    public PassportServiceImpl(PassportRepository passportRepository) {
        this.passportRepository = passportRepository;
    }

    @Override
    public Passport save(Passport passport) {
        log.debug("Request to save Passport : {}", passport);
        return passportRepository.save(passport);
    }

    @Override
    public Passport update(Passport passport) {
        log.debug("Request to save Passport : {}", passport);
        return passportRepository.save(passport);
    }

    @Override
    public Optional<Passport> partialUpdate(Passport passport) {
        log.debug("Request to partially update Passport : {}", passport);

        return passportRepository
            .findById(passport.getId())
            .map(existingPassport -> {
                if (passport.getIdentity() != null) {
                    existingPassport.setIdentity(passport.getIdentity());
                }
                if (passport.getExpiry() != null) {
                    existingPassport.setExpiry(passport.getExpiry());
                }
                if (passport.getIssuingCountry() != null) {
                    existingPassport.setIssuingCountry(passport.getIssuingCountry());
                }
                if (passport.getDocumentNumber() != null) {
                    existingPassport.setDocumentNumber(passport.getDocumentNumber());
                }
                if (passport.getPassportType() != null) {
                    existingPassport.setPassportType(passport.getPassportType());
                }

                return existingPassport;
            })
            .map(passportRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Passport> findAll() {
        log.debug("Request to get all Passports");
        return passportRepository.findAll();
    }

    /**
     *  Get all the passports where Customer is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Passport> findAllWhereCustomerIsNull() {
        log.debug("Request to get all passports where Customer is null");
        return StreamSupport
            .stream(passportRepository.findAll().spliterator(), false)
            .filter(passport -> passport.getCustomer() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Passport> findOne(Long id) {
        log.debug("Request to get Passport : {}", id);
        return passportRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Passport : {}", id);
        passportRepository.deleteById(id);
    }
}
