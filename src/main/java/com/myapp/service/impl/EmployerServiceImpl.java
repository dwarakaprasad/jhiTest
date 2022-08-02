package com.myapp.service.impl;

import com.myapp.domain.Employer;
import com.myapp.repository.EmployerRepository;
import com.myapp.service.EmployerService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Employer}.
 */
@Service
@Transactional
public class EmployerServiceImpl implements EmployerService {

    private final Logger log = LoggerFactory.getLogger(EmployerServiceImpl.class);

    private final EmployerRepository employerRepository;

    public EmployerServiceImpl(EmployerRepository employerRepository) {
        this.employerRepository = employerRepository;
    }

    @Override
    public Employer save(Employer employer) {
        log.debug("Request to save Employer : {}", employer);
        return employerRepository.save(employer);
    }

    @Override
    public Employer update(Employer employer) {
        log.debug("Request to save Employer : {}", employer);
        return employerRepository.save(employer);
    }

    @Override
    public Optional<Employer> partialUpdate(Employer employer) {
        log.debug("Request to partially update Employer : {}", employer);

        return employerRepository
            .findById(employer.getId())
            .map(existingEmployer -> {
                if (employer.getName() != null) {
                    existingEmployer.setName(employer.getName());
                }
                if (employer.getEin() != null) {
                    existingEmployer.setEin(employer.getEin());
                }
                if (employer.getStarted() != null) {
                    existingEmployer.setStarted(employer.getStarted());
                }
                if (employer.getEmployerType() != null) {
                    existingEmployer.setEmployerType(employer.getEmployerType());
                }

                return existingEmployer;
            })
            .map(employerRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employer> findAll() {
        log.debug("Request to get all Employers");
        return employerRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Employer> findOne(Long id) {
        log.debug("Request to get Employer : {}", id);
        return employerRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Employer : {}", id);
        employerRepository.deleteById(id);
    }
}
