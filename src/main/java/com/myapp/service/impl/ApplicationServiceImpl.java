package com.myapp.service.impl;

import com.myapp.domain.Application;
import com.myapp.repository.ApplicationRepository;
import com.myapp.service.ApplicationService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Application}.
 */
@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private final Logger log = LoggerFactory.getLogger(ApplicationServiceImpl.class);

    private final ApplicationRepository applicationRepository;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Override
    public Application save(Application application) {
        log.debug("Request to save Application : {}", application);
        return applicationRepository.save(application);
    }

    @Override
    public Application update(Application application) {
        log.debug("Request to save Application : {}", application);
        return applicationRepository.save(application);
    }

    @Override
    public Optional<Application> partialUpdate(Application application) {
        log.debug("Request to partially update Application : {}", application);

        return applicationRepository
            .findById(application.getId())
            .map(existingApplication -> {
                if (application.getApplicationType() != null) {
                    existingApplication.setApplicationType(application.getApplicationType());
                }
                if (application.getInitiationdate() != null) {
                    existingApplication.setInitiationdate(application.getInitiationdate());
                }
                if (application.getSubmissionDate() != null) {
                    existingApplication.setSubmissionDate(application.getSubmissionDate());
                }
                if (application.getFinalizationdate() != null) {
                    existingApplication.setFinalizationdate(application.getFinalizationdate());
                }
                if (application.getApplicationIdentifier() != null) {
                    existingApplication.setApplicationIdentifier(application.getApplicationIdentifier());
                }

                return existingApplication;
            })
            .map(applicationRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Application> findAll() {
        log.debug("Request to get all Applications");
        return applicationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Application> findOne(Long id) {
        log.debug("Request to get Application : {}", id);
        return applicationRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Application : {}", id);
        applicationRepository.deleteById(id);
    }
}
