package com.acmebank.accountmanager.dao;

import com.acmebank.accountmanager.model.Account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
//    public String createAccount() {
//        return null;
//    }
//
//    private Account getAccount(String accountNumber) {
//        return null;
//    }
//
//    public Account updateAccount(String accountNumber, Double balance) {
//        return null;
//    }
//
//    public boolean deleteAccount(String accountNumber) {
//        return false;
//    }
    Account findByAccountNumber(String accountNumber);
}
