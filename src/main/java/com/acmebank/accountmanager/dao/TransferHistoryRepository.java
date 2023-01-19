package com.acmebank.accountmanager.dao;

import com.acmebank.accountmanager.model.TransferHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferHistoryRepository extends CrudRepository<TransferHistory, Long> {
}
