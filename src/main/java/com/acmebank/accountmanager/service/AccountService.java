package com.acmebank.accountmanager.service;

import com.acmebank.accountmanager.dao.AccountRepository;
import com.acmebank.accountmanager.dto.TransferPayloadDTO;
import com.acmebank.accountmanager.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

    public Account getAccount(String accountNumber) {
        log.info("getting account with account number: " + accountNumber);
        Account account = this.accountRepository.findByAccountNumber(accountNumber);
        log.info("account balance: " + account.getBalance());
        return account;
    }

    private void updateBalance(Account account, Double adjustment) {
        account.setBalance(adjustment);
    }

    public Account transferTo(String sourceAccountNumber, TransferPayloadDTO payloadDTO) {
        log.info("getting account with account number: " + sourceAccountNumber);
        Double transferAmount = payloadDTO.getTransferAmount();
        String targetAccountNumber = payloadDTO.getTargetAccountNumber();;
        Account sourceAccount = this.getAccount(sourceAccountNumber);
        if (sourceAccount.getBalance() > 0 && sourceAccount.getBalance() >= transferAmount) {
            log.info("got enough balance to transfer to other account");

            Account targetAccount = this.getAccount(targetAccountNumber);
            try {
                this.updateBalance(sourceAccount, -1 * transferAmount);
                this.updateBalance(targetAccount, transferAmount);
                // TODO Add Transfer Historu
            } catch (Exception ex) {
                log.error("transfer unsuccesful. investigation required");
            }
        } else {
            log.info("not enough balance to transfer to other account");
        }

        return sourceAccount;
    }
}
