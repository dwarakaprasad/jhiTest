package com.myapp.service.impl;

import com.myapp.domain.Assets;
import com.myapp.repository.AssetsRepository;
import com.myapp.service.AssetsService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Assets}.
 */
@Service
@Transactional
public class AssetsServiceImpl implements AssetsService {

    private final Logger log = LoggerFactory.getLogger(AssetsServiceImpl.class);

    private final AssetsRepository assetsRepository;

    public AssetsServiceImpl(AssetsRepository assetsRepository) {
        this.assetsRepository = assetsRepository;
    }

    @Override
    public Assets save(Assets assets) {
        log.debug("Request to save Assets : {}", assets);
        return assetsRepository.save(assets);
    }

    @Override
    public Assets update(Assets assets) {
        log.debug("Request to save Assets : {}", assets);
        return assetsRepository.save(assets);
    }

    @Override
    public Optional<Assets> partialUpdate(Assets assets) {
        log.debug("Request to partially update Assets : {}", assets);

        return assetsRepository
            .findById(assets.getId())
            .map(existingAssets -> {
                if (assets.getName() != null) {
                    existingAssets.setName(assets.getName());
                }
                if (assets.getAssetType() != null) {
                    existingAssets.setAssetType(assets.getAssetType());
                }

                return existingAssets;
            })
            .map(assetsRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Assets> findAll() {
        log.debug("Request to get all Assets");
        return assetsRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Assets> findOne(Long id) {
        log.debug("Request to get Assets : {}", id);
        return assetsRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Assets : {}", id);
        assetsRepository.deleteById(id);
    }
}
