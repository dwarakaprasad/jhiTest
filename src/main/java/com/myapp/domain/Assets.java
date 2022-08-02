package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.myapp.domain.enumeration.ASSETTYPE;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Assets.
 */
@Entity
@Table(name = "assets")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Assets implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "asset_type", nullable = false)
    private ASSETTYPE assetType;

    @JsonIgnoreProperties(value = { "customer" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Address address;

    @ManyToMany(mappedBy = "assets")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "incomeSources", "assets", "application" }, allowSetters = true)
    private Set<Applicant> applicants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Assets id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Assets name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ASSETTYPE getAssetType() {
        return this.assetType;
    }

    public Assets assetType(ASSETTYPE assetType) {
        this.setAssetType(assetType);
        return this;
    }

    public void setAssetType(ASSETTYPE assetType) {
        this.assetType = assetType;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Assets address(Address address) {
        this.setAddress(address);
        return this;
    }

    public Set<Applicant> getApplicants() {
        return this.applicants;
    }

    public void setApplicants(Set<Applicant> applicants) {
        if (this.applicants != null) {
            this.applicants.forEach(i -> i.removeAssets(this));
        }
        if (applicants != null) {
            applicants.forEach(i -> i.addAssets(this));
        }
        this.applicants = applicants;
    }

    public Assets applicants(Set<Applicant> applicants) {
        this.setApplicants(applicants);
        return this;
    }

    public Assets addApplicant(Applicant applicant) {
        this.applicants.add(applicant);
        applicant.getAssets().add(this);
        return this;
    }

    public Assets removeApplicant(Applicant applicant) {
        this.applicants.remove(applicant);
        applicant.getAssets().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Assets)) {
            return false;
        }
        return id != null && id.equals(((Assets) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Assets{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", assetType='" + getAssetType() + "'" +
            "}";
    }
}
