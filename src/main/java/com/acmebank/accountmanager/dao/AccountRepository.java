package com.acmebank.accountmanager.dao;

import com.acmebank.accountmanager.model.Account;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
    Account findByAccountNumber(String accountNumber);
}
