package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.myapp.domain.enumeration.INCOMETYPE;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A IncomeSource.
 */
@Entity
@Table(name = "income_source")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class IncomeSource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "income_type", nullable = false)
    private INCOMETYPE incomeType;

    @NotNull
    @Column(name = "income_amount", nullable = false)
    private Double incomeAmount;

    @NotNull
    @Column(name = "duration", nullable = false)
    private Long duration;

    @OneToOne
    @JoinColumn(unique = true)
    private Employer employer;

    @ManyToOne
    @JsonIgnoreProperties(value = { "incomeSources", "assets", "application" }, allowSetters = true)
    private Applicant applicant;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IncomeSource id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public INCOMETYPE getIncomeType() {
        return this.incomeType;
    }

    public IncomeSource incomeType(INCOMETYPE incomeType) {
        this.setIncomeType(incomeType);
        return this;
    }

    public void setIncomeType(INCOMETYPE incomeType) {
        this.incomeType = incomeType;
    }

    public Double getIncomeAmount() {
        return this.incomeAmount;
    }

    public IncomeSource incomeAmount(Double incomeAmount) {
        this.setIncomeAmount(incomeAmount);
        return this;
    }

    public void setIncomeAmount(Double incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public Long getDuration() {
        return this.duration;
    }

    public IncomeSource duration(Long duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Employer getEmployer() {
        return this.employer;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
    }

    public IncomeSource employer(Employer employer) {
        this.setEmployer(employer);
        return this;
    }

    public Applicant getApplicant() {
        return this.applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    public IncomeSource applicant(Applicant applicant) {
        this.setApplicant(applicant);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IncomeSource)) {
            return false;
        }
        return id != null && id.equals(((IncomeSource) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IncomeSource{" +
            "id=" + getId() +
            ", incomeType='" + getIncomeType() + "'" +
            ", incomeAmount=" + getIncomeAmount() +
            ", duration=" + getDuration() +
            "}";
    }
}
