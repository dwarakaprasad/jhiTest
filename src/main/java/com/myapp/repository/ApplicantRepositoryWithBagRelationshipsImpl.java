package com.myapp.repository;

import com.myapp.domain.Applicant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ApplicantRepositoryWithBagRelationshipsImpl implements ApplicantRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Applicant> fetchBagRelationships(Optional<Applicant> applicant) {
        return applicant.map(this::fetchAssets);
    }

    @Override
    public Page<Applicant> fetchBagRelationships(Page<Applicant> applicants) {
        return new PageImpl<>(fetchBagRelationships(applicants.getContent()), applicants.getPageable(), applicants.getTotalElements());
    }

    @Override
    public List<Applicant> fetchBagRelationships(List<Applicant> applicants) {
        return Optional.of(applicants).map(this::fetchAssets).orElse(Collections.emptyList());
    }

    Applicant fetchAssets(Applicant result) {
        return entityManager
            .createQuery(
                "select applicant from Applicant applicant left join fetch applicant.assets where applicant is :applicant",
                Applicant.class
            )
            .setParameter("applicant", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Applicant> fetchAssets(List<Applicant> applicants) {
        return entityManager
            .createQuery(
                "select distinct applicant from Applicant applicant left join fetch applicant.assets where applicant in :applicants",
                Applicant.class
            )
            .setParameter("applicants", applicants)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
