package com.myapp.repository;

import com.myapp.domain.IncomeSource;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the IncomeSource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IncomeSourceRepository extends JpaRepository<IncomeSource, Long> {}
