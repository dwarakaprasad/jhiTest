package com.myapp.service.impl;

import com.myapp.domain.IncomeSource;
import com.myapp.repository.IncomeSourceRepository;
import com.myapp.service.IncomeSourceService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link IncomeSource}.
 */
@Service
@Transactional
public class IncomeSourceServiceImpl implements IncomeSourceService {

    private final Logger log = LoggerFactory.getLogger(IncomeSourceServiceImpl.class);

    private final IncomeSourceRepository incomeSourceRepository;

    public IncomeSourceServiceImpl(IncomeSourceRepository incomeSourceRepository) {
        this.incomeSourceRepository = incomeSourceRepository;
    }

    @Override
    public IncomeSource save(IncomeSource incomeSource) {
        log.debug("Request to save IncomeSource : {}", incomeSource);
        return incomeSourceRepository.save(incomeSource);
    }

    @Override
    public IncomeSource update(IncomeSource incomeSource) {
        log.debug("Request to save IncomeSource : {}", incomeSource);
        return incomeSourceRepository.save(incomeSource);
    }

    @Override
    public Optional<IncomeSource> partialUpdate(IncomeSource incomeSource) {
        log.debug("Request to partially update IncomeSource : {}", incomeSource);

        return incomeSourceRepository
            .findById(incomeSource.getId())
            .map(existingIncomeSource -> {
                if (incomeSource.getIncomeType() != null) {
                    existingIncomeSource.setIncomeType(incomeSource.getIncomeType());
                }
                if (incomeSource.getIncomeAmount() != null) {
                    existingIncomeSource.setIncomeAmount(incomeSource.getIncomeAmount());
                }
                if (incomeSource.getDuration() != null) {
                    existingIncomeSource.setDuration(incomeSource.getDuration());
                }

                return existingIncomeSource;
            })
            .map(incomeSourceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncomeSource> findAll() {
        log.debug("Request to get all IncomeSources");
        return incomeSourceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IncomeSource> findOne(Long id) {
        log.debug("Request to get IncomeSource : {}", id);
        return incomeSourceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete IncomeSource : {}", id);
        incomeSourceRepository.deleteById(id);
    }
}
