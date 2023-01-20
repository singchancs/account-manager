package com.acmebank.accountmanager.service;

import com.acmebank.accountmanager.constant.ErrorCode;
import com.acmebank.accountmanager.dao.AccountRepository;
import com.acmebank.accountmanager.dao.TransferHistoryRepository;
import com.acmebank.accountmanager.dto.BalanceDTO;
import com.acmebank.accountmanager.dto.TransferPayloadDTO;
import com.acmebank.accountmanager.dto.TransferStatusDTO;
import com.acmebank.accountmanager.exceptions.EntityNotFoundException;
import com.acmebank.accountmanager.exceptions.TransferFailedException;
import com.acmebank.accountmanager.model.Account;
import com.acmebank.accountmanager.model.TransferHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class AccountService implements IAccountService {
    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransferHistoryRepository transferHistoryRepository;

    private Account getAccount(String accountNumber) throws EntityNotFoundException {
        log.info("getting account with account number: " + accountNumber);
        Account account = this.accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            log.error("account not found: " + accountNumber);
            throw new EntityNotFoundException(Account.class, ErrorCode.ERR_ACME_002, "accountNumber", accountNumber);
        }
        return account;
    }

    private BalanceDTO convertToDto(Account account) {
        BalanceDTO balanceDTO = BalanceDTO.builder()
                .currency(account.getCurrency())
                .balance(account.getBalance())
                .lastUpdatedAt(account.getUpdatedAt())
                .build();
        return balanceDTO;
    }

    private void updateBalance(Account account, BigDecimal adjustment) {
        log.info("updating account " + account.getAccountNumber() + " with balance " + adjustment);
        account.setBalance(adjustment);
        account.setUpdatedAt(new Date());
        accountRepository.save(account);
    }

    @Override
    public BalanceDTO getBalance(String accountNumber) throws EntityNotFoundException {
        log.info("getting account with account number: " + accountNumber);
        Account account = this.getAccount(accountNumber);
        log.info("account balance: " + account.getBalance());
        return this.convertToDto(account);
    }

    @Override
    public TransferStatusDTO transferTo(String sourceAccountNumber, TransferPayloadDTO payloadDTO) throws EntityNotFoundException, TransferFailedException {
        log.info("getting account with account number: " + sourceAccountNumber);
        Date createdAt = new Date();
        TransferStatusDTO.TransferStatusDTOBuilder transferStatusDTOBuilder = TransferStatusDTO.builder()
                .createdAt(createdAt)
                .amount(payloadDTO.getTransferAmount())
                .currency(payloadDTO.getTransferCurrency())
                .transferTo(payloadDTO.getTargetAccountNumber());
        BigDecimal transferAmount = payloadDTO.getTransferAmount();
        String targetAccountNumber = payloadDTO.getTargetAccountNumber();

        TransferHistory.TransferHistoryBuilder builder = TransferHistory.builder()
                .createdAt(createdAt)
                .transferAmount(transferAmount);

        Account sourceAccount = null;
        try {
            sourceAccount = this.getAccount(sourceAccountNumber);
            builder.sourceAccountNumber(sourceAccountNumber);
        } catch (EntityNotFoundException ex) {
           String remark = "source account not found " + sourceAccountNumber;

            builder.isSuccessful(false)
                    .errorCode(ErrorCode.ERR_ACME_002)
                    .remark(remark);
            transferHistoryRepository.save(builder.build());

            throw ex;
        }

        Account targetAccount = null;
        try {
            targetAccount = this.getAccount(targetAccountNumber);
            builder.targetAccountNumber(targetAccountNumber);
        } catch (EntityNotFoundException ex) {
            log.error("target account not found for this transfer " + targetAccountNumber);

            String remark = "target account not found " + targetAccountNumber;
            builder.isSuccessful(false)
                    .errorCode(ErrorCode.ERR_ACME_002)
                    .remark(remark);
            transferHistoryRepository.save(builder.build());

            throw ex;
        }

        if (sourceAccountNumber.equalsIgnoreCase(targetAccountNumber)) {

            String remark = "Source account is the same as target account";
            builder.isSuccessful(false)
                    .errorCode(ErrorCode.ERR_ACME_004)
                    .remark(remark);
            transferHistoryRepository.save(builder.build());

            throw new TransferFailedException(ErrorCode.ERR_ACME_004, remark);
        }

        if (transferAmount.compareTo(new BigDecimal("0.0001")) == -1) {

            String remark = "Invalid transfer amount. Must be greater than 0.01";
            builder.isSuccessful(false)
                    .errorCode(ErrorCode.ERR_ACME_005)
                    .remark(remark);
            transferHistoryRepository.save(builder.build());

            throw new TransferFailedException(ErrorCode.ERR_ACME_005, remark);
        }

        BigDecimal sourceOriginalBalance = sourceAccount.getBalance();
        log.info("source account balance: " + sourceOriginalBalance);
        if (sourceOriginalBalance.compareTo(BigDecimal.ZERO) == 1 && (sourceOriginalBalance.compareTo(transferAmount) == 1 || sourceOriginalBalance.compareTo(transferAmount) == 0)) {
            log.info("got enough balance to transfer to other account");
            try {
                BigDecimal targetOriginalBalance = targetAccount.getBalance();
                log.info("target account balance: " + targetOriginalBalance);

                log.info("transferring " + transferAmount + " from " + sourceAccountNumber + " to " + targetAccountNumber);
                this.updateBalance(sourceAccount, sourceOriginalBalance.subtract(transferAmount));
                this.updateBalance(targetAccount, targetOriginalBalance.add(transferAmount));

                builder.isSuccessful(true)
                        .transferAt(new Date());
                transferHistoryRepository.save(builder.build());

                TransferStatusDTO transferStatusDTO = transferStatusDTOBuilder.status("success")
                        .transferAt(new Date())
                        .balance(this.convertToDto(sourceAccount))
                        .build();
                return transferStatusDTO;

            } catch (Exception ex) {
                log.error("transfer unsuccessful. investigation required");

                String remark = "Transfer failed";
                builder.isSuccessful(false)
                        .errorCode(ErrorCode.ERR_ACME_001)
                        .remark(remark);
                transferHistoryRepository.save(builder.build());

                throw new TransferFailedException(ErrorCode.ERR_ACME_001, "Transfer failed", ex);
            }
        } else {
            log.info("not enough balance to transfer to other account");

            String remark = "Insufficient Balance";
            builder.isSuccessful(false)
                    .errorCode(ErrorCode.ERR_ACME_003)
                    .remark("Insufficient Balance");
            transferHistoryRepository.save(builder.build());

            throw new TransferFailedException(ErrorCode.ERR_ACME_003, remark);

        }
    }
}
