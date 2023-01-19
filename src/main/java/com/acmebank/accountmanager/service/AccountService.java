package com.acmebank.accountmanager.service;

import com.acmebank.accountmanager.constant.ErrorCode;
import com.acmebank.accountmanager.dao.AccountRepository;
import com.acmebank.accountmanager.dao.TransferHistoryRepository;
import com.acmebank.accountmanager.dto.BalanceDTO;
import com.acmebank.accountmanager.dto.TransferPayloadDTO;
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
public class AccountService {
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

    public BalanceDTO getBalance(String accountNumber) throws EntityNotFoundException {
        log.info("getting account with account number: " + accountNumber);
        Account account = this.getAccount(accountNumber);
        log.info("account balance: " + account.getBalance());
        return this.convertToDto(account);
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
        account.setBalance(adjustment);
        account.setUpdatedAt(new Date());
        accountRepository.save(account);
    }

    public BalanceDTO transferTo(String sourceAccountNumber, TransferPayloadDTO payloadDTO) throws EntityNotFoundException, TransferFailedException {
        log.info("getting account with account number: " + sourceAccountNumber);
        BigDecimal transferAmount = payloadDTO.getTransferAmount();
        String targetAccountNumber = payloadDTO.getTargetAccountNumber();
        Account sourceAccount = this.getAccount(sourceAccountNumber);
        if (sourceAccountNumber.equalsIgnoreCase(targetAccountNumber)) {
            throw new TransferFailedException(ErrorCode.ERR_ACME_004, "Source account is the same as target account");
        }

        if (transferAmount.compareTo(BigDecimal.ZERO) == -1 || transferAmount.compareTo(BigDecimal.ZERO) == 0) {
            throw new TransferFailedException(ErrorCode.ERR_ACME_005, "Invalid transfer amount. Must be greater than 0");
        }

        TransferHistory.TransferHistoryBuilder builder = TransferHistory.builder()
                .sourceAccountNumber(sourceAccountNumber)
                .targetAccountNumber(targetAccountNumber)
                .transferAmount(transferAmount)
                .transferAt(new Date());

        BigDecimal sourceOriginalBalance = sourceAccount.getBalance();
        if (sourceOriginalBalance.compareTo(BigDecimal.ZERO) == 1 && (sourceOriginalBalance.compareTo(transferAmount) == 1 || sourceOriginalBalance.compareTo(transferAmount) == 0)) {
            log.info("got enough balance to transfer to other account");

            Account targetAccount = this.getAccount(targetAccountNumber);
            BigDecimal targetOriginalBalance = targetAccount.getBalance();
            try {
                log.info("transferring " + transferAmount + " from " + sourceAccountNumber + " to " + targetAccountNumber);
                this.updateBalance(sourceAccount, sourceOriginalBalance.subtract(transferAmount));
                this.updateBalance(targetAccount, targetOriginalBalance.add(transferAmount));
                builder.isSuccessful(true);
            } catch (Exception ex) {
                builder.isSuccessful(false);
                log.error("transfer unsuccessful. investigation required");
                throw new TransferFailedException(ErrorCode.ERR_ACME_001, "Transfer failed", ex);
            }
        } else {
            log.info("not enough balance to transfer to other account");
            throw new TransferFailedException(ErrorCode.ERR_ACME_003, "Insufficient Balance");

        }
        TransferHistory transferHistory = builder.build();
        transferHistoryRepository.save(transferHistory);
        return this.convertToDto(sourceAccount);
    }
}
