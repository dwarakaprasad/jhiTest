package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.myapp.domain.enumeration.APPLICATIONTYPE;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Application.
 */
@Entity
@Table(name = "application")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Application implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "application_type", nullable = false)
    private APPLICATIONTYPE applicationType;

    @NotNull
    @Column(name = "initiationdate", nullable = false)
    private LocalDate initiationdate;

    @NotNull
    @Column(name = "submission_date", nullable = false)
    private LocalDate submissionDate;

    @NotNull
    @Column(name = "finalizationdate", nullable = false)
    private LocalDate finalizationdate;

    @Type(type = "uuid-char")
    @Column(name = "application_identifier", length = 36)
    private UUID applicationIdentifier;

    @OneToMany(mappedBy = "application")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "incomeSources", "assets", "application" }, allowSetters = true)
    private Set<Applicant> applicants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Application id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public APPLICATIONTYPE getApplicationType() {
        return this.applicationType;
    }

    public Application applicationType(APPLICATIONTYPE applicationType) {
        this.setApplicationType(applicationType);
        return this;
    }

    public void setApplicationType(APPLICATIONTYPE applicationType) {
        this.applicationType = applicationType;
    }

    public LocalDate getInitiationdate() {
        return this.initiationdate;
    }

    public Application initiationdate(LocalDate initiationdate) {
        this.setInitiationdate(initiationdate);
        return this;
    }

    public void setInitiationdate(LocalDate initiationdate) {
        this.initiationdate = initiationdate;
    }

    public LocalDate getSubmissionDate() {
        return this.submissionDate;
    }

    public Application submissionDate(LocalDate submissionDate) {
        this.setSubmissionDate(submissionDate);
        return this;
    }

    public void setSubmissionDate(LocalDate submissionDate) {
        this.submissionDate = submissionDate;
    }

    public LocalDate getFinalizationdate() {
        return this.finalizationdate;
    }

    public Application finalizationdate(LocalDate finalizationdate) {
        this.setFinalizationdate(finalizationdate);
        return this;
    }

    public void setFinalizationdate(LocalDate finalizationdate) {
        this.finalizationdate = finalizationdate;
    }

    public UUID getApplicationIdentifier() {
        return this.applicationIdentifier;
    }

    public Application applicationIdentifier(UUID applicationIdentifier) {
        this.setApplicationIdentifier(applicationIdentifier);
        return this;
    }

    public void setApplicationIdentifier(UUID applicationIdentifier) {
        this.applicationIdentifier = applicationIdentifier;
    }

    public Set<Applicant> getApplicants() {
        return this.applicants;
    }

    public void setApplicants(Set<Applicant> applicants) {
        if (this.applicants != null) {
            this.applicants.forEach(i -> i.setApplication(null));
        }
        if (applicants != null) {
            applicants.forEach(i -> i.setApplication(this));
        }
        this.applicants = applicants;
    }

    public Application applicants(Set<Applicant> applicants) {
        this.setApplicants(applicants);
        return this;
    }

    public Application addApplicant(Applicant applicant) {
        this.applicants.add(applicant);
        applicant.setApplication(this);
        return this;
    }

    public Application removeApplicant(Applicant applicant) {
        this.applicants.remove(applicant);
        applicant.setApplication(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Application)) {
            return false;
        }
        return id != null && id.equals(((Application) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Application{" +
            "id=" + getId() +
            ", applicationType='" + getApplicationType() + "'" +
            ", initiationdate='" + getInitiationdate() + "'" +
            ", submissionDate='" + getSubmissionDate() + "'" +
            ", finalizationdate='" + getFinalizationdate() + "'" +
            ", applicationIdentifier='" + getApplicationIdentifier() + "'" +
            "}";
    }
}
