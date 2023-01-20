package com.acmebank.accountmanager.service;

import com.acmebank.accountmanager.dto.BalanceDTO;
import com.acmebank.accountmanager.dto.TransferPayloadDTO;
import com.acmebank.accountmanager.dto.TransferStatusDTO;
import com.acmebank.accountmanager.exceptions.EntityNotFoundException;
import com.acmebank.accountmanager.exceptions.TransferFailedException;

public interface IAccountService {

    BalanceDTO getBalance(String accountNumber) throws EntityNotFoundException;
    TransferStatusDTO transferTo(String sourceAccountNumber, TransferPayloadDTO payloadDTO) throws EntityNotFoundException, TransferFailedException;
}
