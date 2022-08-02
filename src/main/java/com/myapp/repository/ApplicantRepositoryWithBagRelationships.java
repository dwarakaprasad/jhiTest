package com.myapp.repository;

import com.myapp.domain.Applicant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ApplicantRepositoryWithBagRelationships {
    Optional<Applicant> fetchBagRelationships(Optional<Applicant> applicant);

    List<Applicant> fetchBagRelationships(List<Applicant> applicants);

    Page<Applicant> fetchBagRelationships(Page<Applicant> applicants);
}
