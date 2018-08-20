package com.softech.dev.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Cart.
 */
@Entity
@Table(name = "cart")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "cart")
public class Cart implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "norm_cart")
    private String normCart;

    @Column(name = "createddate")
    private Instant createddate;

    @Column(name = "lastactivedate")
    private Instant lastactivedate;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "checkout")
    private Boolean checkout;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNormCart() {
        return normCart;
    }

    public Cart normCart(String normCart) {
        this.normCart = normCart;
        return this;
    }

    public void setNormCart(String normCart) {
        this.normCart = normCart;
    }

    public Instant getCreateddate() {
        return createddate;
    }

    public Cart createddate(Instant createddate) {
        this.createddate = createddate;
        return this;
    }

    public void setCreateddate(Instant createddate) {
        this.createddate = createddate;
    }

    public Instant getLastactivedate() {
        return lastactivedate;
    }

    public Cart lastactivedate(Instant lastactivedate) {
        this.lastactivedate = lastactivedate;
        return this;
    }

    public void setLastactivedate(Instant lastactivedate) {
        this.lastactivedate = lastactivedate;
    }

    public Double getAmount() {
        return amount;
    }

    public Cart amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Boolean isCheckout() {
        return checkout;
    }

    public Cart checkout(Boolean checkout) {
        this.checkout = checkout;
        return this;
    }

    public void setCheckout(Boolean checkout) {
        this.checkout = checkout;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Cart customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cart cart = (Cart) o;
        if (cart.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cart.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Cart{" +
            "id=" + getId() +
            ", normCart='" + getNormCart() + "'" +
            ", createddate='" + getCreateddate() + "'" +
            ", lastactivedate='" + getLastactivedate() + "'" +
            ", amount=" + getAmount() +
            ", checkout='" + isCheckout() + "'" +
            "}";
    }
}
