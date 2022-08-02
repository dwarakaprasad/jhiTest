package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.myapp.domain.enumeration.PAYMENTTYPE;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PaymentInfo.
 */
@Entity
@Table(name = "payment_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PaymentInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PAYMENTTYPE paymentType;

    @NotNull
    @Column(name = "number", nullable = false)
    private Long number;

    @Column(name = "expiry")
    private LocalDate expiry;

    @Column(name = "security")
    private Long security;

    @ManyToOne
    @JsonIgnoreProperties(value = { "passport", "paymentInfos", "addresses" }, allowSetters = true)
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PaymentInfo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PAYMENTTYPE getPaymentType() {
        return this.paymentType;
    }

    public PaymentInfo paymentType(PAYMENTTYPE paymentType) {
        this.setPaymentType(paymentType);
        return this;
    }

    public void setPaymentType(PAYMENTTYPE paymentType) {
        this.paymentType = paymentType;
    }

    public Long getNumber() {
        return this.number;
    }

    public PaymentInfo number(Long number) {
        this.setNumber(number);
        return this;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public LocalDate getExpiry() {
        return this.expiry;
    }

    public PaymentInfo expiry(LocalDate expiry) {
        this.setExpiry(expiry);
        return this;
    }

    public void setExpiry(LocalDate expiry) {
        this.expiry = expiry;
    }

    public Long getSecurity() {
        return this.security;
    }

    public PaymentInfo security(Long security) {
        this.setSecurity(security);
        return this;
    }

    public void setSecurity(Long security) {
        this.security = security;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public PaymentInfo customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentInfo)) {
            return false;
        }
        return id != null && id.equals(((PaymentInfo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentInfo{" +
            "id=" + getId() +
            ", paymentType='" + getPaymentType() + "'" +
            ", number=" + getNumber() +
            ", expiry='" + getExpiry() + "'" +
            ", security=" + getSecurity() +
            "}";
    }
}
