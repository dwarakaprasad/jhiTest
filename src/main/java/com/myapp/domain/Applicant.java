package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.myapp.domain.enumeration.GENDER;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Applicant.
 */
@Entity
@Table(name = "applicant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Applicant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private GENDER gender;

    @OneToMany(mappedBy = "applicant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "employer", "applicant" }, allowSetters = true)
    private Set<IncomeSource> incomeSources = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_applicant__assets",
        joinColumns = @JoinColumn(name = "applicant_id"),
        inverseJoinColumns = @JoinColumn(name = "assets_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "address", "applicants" }, allowSetters = true)
    private Set<Assets> assets = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "applicants" }, allowSetters = true)
    private Application application;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Applicant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Applicant firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public Applicant middleName(String middleName) {
        this.setMiddleName(middleName);
        return this;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Applicant lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDob() {
        return this.dob;
    }

    public Applicant dob(LocalDate dob) {
        this.setDob(dob);
        return this;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public GENDER getGender() {
        return this.gender;
    }

    public Applicant gender(GENDER gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(GENDER gender) {
        this.gender = gender;
    }

    public Set<IncomeSource> getIncomeSources() {
        return this.incomeSources;
    }

    public void setIncomeSources(Set<IncomeSource> incomeSources) {
        if (this.incomeSources != null) {
            this.incomeSources.forEach(i -> i.setApplicant(null));
        }
        if (incomeSources != null) {
            incomeSources.forEach(i -> i.setApplicant(this));
        }
        this.incomeSources = incomeSources;
    }

    public Applicant incomeSources(Set<IncomeSource> incomeSources) {
        this.setIncomeSources(incomeSources);
        return this;
    }

    public Applicant addIncomeSource(IncomeSource incomeSource) {
        this.incomeSources.add(incomeSource);
        incomeSource.setApplicant(this);
        return this;
    }

    public Applicant removeIncomeSource(IncomeSource incomeSource) {
        this.incomeSources.remove(incomeSource);
        incomeSource.setApplicant(null);
        return this;
    }

    public Set<Assets> getAssets() {
        return this.assets;
    }

    public void setAssets(Set<Assets> assets) {
        this.assets = assets;
    }

    public Applicant assets(Set<Assets> assets) {
        this.setAssets(assets);
        return this;
    }

    public Applicant addAssets(Assets assets) {
        this.assets.add(assets);
        assets.getApplicants().add(this);
        return this;
    }

    public Applicant removeAssets(Assets assets) {
        this.assets.remove(assets);
        assets.getApplicants().remove(this);
        return this;
    }

    public Application getApplication() {
        return this.application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Applicant application(Application application) {
        this.setApplication(application);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Applicant)) {
            return false;
        }
        return id != null && id.equals(((Applicant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Applicant{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", middleName='" + getMiddleName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", dob='" + getDob() + "'" +
            ", gender='" + getGender() + "'" +
            "}";
    }
}
