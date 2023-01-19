package com.acmebank.accountmanager.dto;

public class BalanceDTO {

    private String currency;
    private Double balance;


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
