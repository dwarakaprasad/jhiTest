package com.myapp.repository;

import com.myapp.domain.PaymentInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PaymentInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, Long> {}
