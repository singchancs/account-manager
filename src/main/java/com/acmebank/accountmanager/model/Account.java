package com.acmebank.accountmanager.model;

import com.acmebank.accountmanager.Currency;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(updatable = false, unique = true, nullable = false, length = 8)
    private String accountNumber;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 3)
    private Currency currency;
    @Column(nullable = false, precision = 20, scale = 4)
    private BigDecimal balance;
    @Column(updatable = false, nullable = false, length = 100)
    private String ownedBy;
    @Column(updatable = false, nullable = false, length = 100)
    private String createdBy;
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(updatable = false, nullable = false)
    private Date createdAt;
    @Column(nullable = false, length = 100)
    private String updatedBy;
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date updatedAt;
    @Column(nullable = false)
    private Boolean isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(String ownedBy) {
        this.ownedBy = ownedBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
