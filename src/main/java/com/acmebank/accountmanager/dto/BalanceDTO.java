package com.acmebank.accountmanager.dto;

import com.acmebank.accountmanager.Currency;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class BalanceDTO {

    private Currency currency;
    private Double balance;
    private Date lastUpdatedAt;


    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }


    public Date getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(Date lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }
}
