package com.acmebank.accountmanager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(updatable = false, nullable = false, length = 8)
    private String sourceAccountNumber;
    @Column(updatable = false, nullable = false, length = 8)
    private String targetAccountNumber;
    @Column(updatable = false, nullable = false)
    private Date createdAt;
    @Column(updatable = false, nullable = false, precision = 20, scale = 4)
    private BigDecimal transferAmount;
    @Column(updatable = false)
    private Date transferAt;
    @Column(nullable = false)
    private Boolean isSuccessful;
    @Column
    private String errorCode;
    @Column
    private String remark;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public void setSourceAccountNumber(String sourceAccountNumber) {
        this.sourceAccountNumber = sourceAccountNumber;
    }

    public String getTargetAccountNumber() {
        return targetAccountNumber;
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

    public Date getTransferAt() {
        return transferAt;
    }

    public void setTransferAt(Date transferAt) {
        this.transferAt = transferAt;
    }

    public Boolean getSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(Boolean successful) {
        isSuccessful = successful;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
