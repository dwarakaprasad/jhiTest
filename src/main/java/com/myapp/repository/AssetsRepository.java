package com.myapp.repository;

import com.myapp.domain.Assets;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Assets entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssetsRepository extends JpaRepository<Assets, Long> {}
