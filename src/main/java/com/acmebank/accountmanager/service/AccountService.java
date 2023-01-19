package com.acmebank.accountmanager.service;

import com.acmebank.accountmanager.constant.ErrorCode;
import com.acmebank.accountmanager.dao.AccountRepository;
import com.acmebank.accountmanager.dao.TransferHistoryRepository;
import com.acmebank.accountmanager.dto.BalanceDTO;
import com.acmebank.accountmanager.dto.TransferPayloadDTO;
import com.acmebank.accountmanager.model.Account;
import com.acmebank.accountmanager.model.TransferHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AccountService {
    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransferHistoryRepository transferHistoryRepository;

    private Account getAccount(String accountNumber) throws Exception {
        log.info("getting account with account number: " + accountNumber);
        Account account = this.accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            //TODO Handle Account Not Found Error
            log.error("account not found: " + accountNumber);
            throw new Exception("Account not found");
        }
        return account;
    }

    public BalanceDTO getBalance(String accountNumber) throws Exception {
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

    private void updateBalance(Account account, Double adjustment) {
        account.setBalance(adjustment);
        accountRepository.save(account);
    }

    public BalanceDTO transferTo(String sourceAccountNumber, TransferPayloadDTO payloadDTO) throws Exception {
        log.info("getting account with account number: " + sourceAccountNumber);
        Double transferAmount = payloadDTO.getTransferAmount();
        String targetAccountNumber = payloadDTO.getTargetAccountNumber();;
        Account sourceAccount = this.getAccount(sourceAccountNumber);

        TransferHistory.TransferHistoryBuilder builder = TransferHistory.builder()
                .sourceAccountNumber(sourceAccountNumber)
                .targetAccountNumber(targetAccountNumber)
                .transferAmount(transferAmount)
                .transferAt(new Date());
        if (sourceAccount.getBalance() > 0 && sourceAccount.getBalance() >= transferAmount) {
            log.info("got enough balance to transfer to other account");

            Account targetAccount = this.getAccount(targetAccountNumber);
            try {
                log.info("transferring " + transferAmount + " from " + sourceAccountNumber + " to " + targetAccountNumber);
                this.updateBalance(sourceAccount, -1 * transferAmount);
                this.updateBalance(targetAccount, transferAmount);
                builder.isSuccessful(true);

            } catch (Exception ex) {
                builder.isSuccessful(false);
                log.error("transfer unsuccessful. investigation required");
            }
        } else {
            log.info("not enough balance to transfer to other account");
            builder.isSuccessful(false)
                    .errorCode(ErrorCode.ERR_ACME_002)
                    .remark("insufficient balance");
        }
        TransferHistory transferHistory = builder.build();
        transferHistoryRepository.save(transferHistory);
        return this.convertToDto(sourceAccount);
    }
}
