package com.myapp.service.impl;

import com.myapp.domain.Applicant;
import com.myapp.repository.ApplicantRepository;
import com.myapp.service.ApplicantService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Applicant}.
 */
@Service
@Transactional
public class ApplicantServiceImpl implements ApplicantService {

    private final Logger log = LoggerFactory.getLogger(ApplicantServiceImpl.class);

    private final ApplicantRepository applicantRepository;

    public ApplicantServiceImpl(ApplicantRepository applicantRepository) {
        this.applicantRepository = applicantRepository;
    }

    @Override
    public Applicant save(Applicant applicant) {
        log.debug("Request to save Applicant : {}", applicant);
        return applicantRepository.save(applicant);
    }

    @Override
    public Applicant update(Applicant applicant) {
        log.debug("Request to save Applicant : {}", applicant);
        return applicantRepository.save(applicant);
    }

    @Override
    public Optional<Applicant> partialUpdate(Applicant applicant) {
        log.debug("Request to partially update Applicant : {}", applicant);

        return applicantRepository
            .findById(applicant.getId())
            .map(existingApplicant -> {
                if (applicant.getFirstName() != null) {
                    existingApplicant.setFirstName(applicant.getFirstName());
                }
                if (applicant.getMiddleName() != null) {
                    existingApplicant.setMiddleName(applicant.getMiddleName());
                }
                if (applicant.getLastName() != null) {
                    existingApplicant.setLastName(applicant.getLastName());
                }
                if (applicant.getDob() != null) {
                    existingApplicant.setDob(applicant.getDob());
                }
                if (applicant.getGender() != null) {
                    existingApplicant.setGender(applicant.getGender());
                }

                return existingApplicant;
            })
            .map(applicantRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Applicant> findAll() {
        log.debug("Request to get all Applicants");
        return applicantRepository.findAllWithEagerRelationships();
    }

    public Page<Applicant> findAllWithEagerRelationships(Pageable pageable) {
        return applicantRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Applicant> findOne(Long id) {
        log.debug("Request to get Applicant : {}", id);
        return applicantRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Applicant : {}", id);
        applicantRepository.deleteById(id);
    }
}
