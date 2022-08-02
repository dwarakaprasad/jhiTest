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
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Customer implements Serializable {

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

    @JsonIgnoreProperties(value = { "customer" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Passport passport;

    @OneToMany(mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "customer" }, allowSetters = true)
    private Set<PaymentInfo> paymentInfos = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "customer" }, allowSetters = true)
    private Set<Address> addresses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Customer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Customer firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public Customer middleName(String middleName) {
        this.setMiddleName(middleName);
        return this;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Customer lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDob() {
        return this.dob;
    }

    public Customer dob(LocalDate dob) {
        this.setDob(dob);
        return this;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public GENDER getGender() {
        return this.gender;
    }

    public Customer gender(GENDER gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(GENDER gender) {
        this.gender = gender;
    }

    public Passport getPassport() {
        return this.passport;
    }

    public void setPassport(Passport passport) {
        this.passport = passport;
    }

    public Customer passport(Passport passport) {
        this.setPassport(passport);
        return this;
    }

    public Set<PaymentInfo> getPaymentInfos() {
        return this.paymentInfos;
    }

    public void setPaymentInfos(Set<PaymentInfo> paymentInfos) {
        if (this.paymentInfos != null) {
            this.paymentInfos.forEach(i -> i.setCustomer(null));
        }
        if (paymentInfos != null) {
            paymentInfos.forEach(i -> i.setCustomer(this));
        }
        this.paymentInfos = paymentInfos;
    }

    public Customer paymentInfos(Set<PaymentInfo> paymentInfos) {
        this.setPaymentInfos(paymentInfos);
        return this;
    }

    public Customer addPaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfos.add(paymentInfo);
        paymentInfo.setCustomer(this);
        return this;
    }

    public Customer removePaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfos.remove(paymentInfo);
        paymentInfo.setCustomer(null);
        return this;
    }

    public Set<Address> getAddresses() {
        return this.addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        if (this.addresses != null) {
            this.addresses.forEach(i -> i.setCustomer(null));
        }
        if (addresses != null) {
            addresses.forEach(i -> i.setCustomer(this));
        }
        this.addresses = addresses;
    }

    public Customer addresses(Set<Address> addresses) {
        this.setAddresses(addresses);
        return this;
    }

    public Customer addAddress(Address address) {
        this.addresses.add(address);
        address.setCustomer(this);
        return this;
    }

    public Customer removeAddress(Address address) {
        this.addresses.remove(address);
        address.setCustomer(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return id != null && id.equals(((Customer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", middleName='" + getMiddleName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", dob='" + getDob() + "'" +
            ", gender='" + getGender() + "'" +
            "}";
    }
}
