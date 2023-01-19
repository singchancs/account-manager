package com.acmebank.accountmanager.dto;

import com.acmebank.accountmanager.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TransferPayloadDTO {

    private String targetAccountNumber;

    private Currency transferCurrency;
    private Double transferAmount;

    public TransferPayloadDTO() {
        this.transferCurrency = Currency.HKD; // Default as HKD
    }

    public String getTargetAccountNumber() {
        return targetAccountNumber;
    }

    public Currency getTransferCurrency() {
        return transferCurrency;
    }

    public void setTransferCurrency(Currency transferCurrency) {
        this.transferCurrency = transferCurrency;
    }

    public void setTargetAccountNumber(String targetAccountNumber) {
        this.targetAccountNumber = targetAccountNumber;
    }


    public Double getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(Double transferAmount) {
        this.transferAmount = transferAmount;
    }
}
