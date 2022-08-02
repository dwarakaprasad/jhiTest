package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.myapp.domain.enumeration.PASSPORTTYPE;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Passport.
 */
@Entity
@Table(name = "passport")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Passport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "identity", nullable = false)
    private String identity;

    @NotNull
    @Column(name = "expiry", nullable = false)
    private LocalDate expiry;

    @NotNull
    @Column(name = "issuing_country", nullable = false)
    private String issuingCountry;

    @Column(name = "document_number")
    private String documentNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "passport_type", nullable = false)
    private PASSPORTTYPE passportType;

    @JsonIgnoreProperties(value = { "passport", "paymentInfos", "addresses" }, allowSetters = true)
    @OneToOne(mappedBy = "passport")
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Passport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentity() {
        return this.identity;
    }

    public Passport identity(String identity) {
        this.setIdentity(identity);
        return this;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public LocalDate getExpiry() {
        return this.expiry;
    }

    public Passport expiry(LocalDate expiry) {
        this.setExpiry(expiry);
        return this;
    }

    public void setExpiry(LocalDate expiry) {
        this.expiry = expiry;
    }

    public String getIssuingCountry() {
        return this.issuingCountry;
    }

    public Passport issuingCountry(String issuingCountry) {
        this.setIssuingCountry(issuingCountry);
        return this;
    }

    public void setIssuingCountry(String issuingCountry) {
        this.issuingCountry = issuingCountry;
    }

    public String getDocumentNumber() {
        return this.documentNumber;
    }

    public Passport documentNumber(String documentNumber) {
        this.setDocumentNumber(documentNumber);
        return this;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public PASSPORTTYPE getPassportType() {
        return this.passportType;
    }

    public Passport passportType(PASSPORTTYPE passportType) {
        this.setPassportType(passportType);
        return this;
    }

    public void setPassportType(PASSPORTTYPE passportType) {
        this.passportType = passportType;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        if (this.customer != null) {
            this.customer.setPassport(null);
        }
        if (customer != null) {
            customer.setPassport(this);
        }
        this.customer = customer;
    }

    public Passport customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Passport)) {
            return false;
        }
        return id != null && id.equals(((Passport) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Passport{" +
            "id=" + getId() +
            ", identity='" + getIdentity() + "'" +
            ", expiry='" + getExpiry() + "'" +
            ", issuingCountry='" + getIssuingCountry() + "'" +
            ", documentNumber='" + getDocumentNumber() + "'" +
            ", passportType='" + getPassportType() + "'" +
            "}";
    }
}
