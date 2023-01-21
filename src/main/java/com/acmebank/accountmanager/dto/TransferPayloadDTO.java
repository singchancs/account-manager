package com.acmebank.accountmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class TransferPayloadDTO {

    @NotBlank
    private String targetAccountNumber;
    @NotBlank
    private String transferCurrency;
    @NotNull
    @DecimalMin(value = "0.0", message = "To must be greater than zero")
    private BigDecimal transferAmount;

    public String getTargetAccountNumber() {
        return targetAccountNumber;
    }

    public String getTransferCurrency() {
        return transferCurrency;
    }

    public void setTransferCurrency(String transferCurrency) {
        this.transferCurrency = transferCurrency;
    }

    public void setTargetAccountNumber(String targetAccountNumber) {
        this.targetAccountNumber = targetAccountNumber;
    }


    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }
}
