package com.myapp.domain;

import com.myapp.domain.enumeration.EMPLOYERTYPE;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Employer.
 */
@Entity
@Table(name = "employer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Employer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "ein", nullable = false)
    private Long ein;

    @Column(name = "started")
    private LocalDate started;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "employer_type", nullable = false)
    private EMPLOYERTYPE employerType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Employer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Employer name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getEin() {
        return this.ein;
    }

    public Employer ein(Long ein) {
        this.setEin(ein);
        return this;
    }

    public void setEin(Long ein) {
        this.ein = ein;
    }

    public LocalDate getStarted() {
        return this.started;
    }

    public Employer started(LocalDate started) {
        this.setStarted(started);
        return this;
    }

    public void setStarted(LocalDate started) {
        this.started = started;
    }

    public EMPLOYERTYPE getEmployerType() {
        return this.employerType;
    }

    public Employer employerType(EMPLOYERTYPE employerType) {
        this.setEmployerType(employerType);
        return this;
    }

    public void setEmployerType(EMPLOYERTYPE employerType) {
        this.employerType = employerType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employer)) {
            return false;
        }
        return id != null && id.equals(((Employer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employer{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", ein=" + getEin() +
            ", started='" + getStarted() + "'" +
            ", employerType='" + getEmployerType() + "'" +
            "}";
    }
}
