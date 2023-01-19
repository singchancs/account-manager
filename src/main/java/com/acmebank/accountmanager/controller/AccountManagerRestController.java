package com.acmebank.accountmanager.controller;

import com.acmebank.accountmanager.dto.BalanceDTO;
import com.acmebank.accountmanager.dto.TransferPayloadDTO;
import com.acmebank.accountmanager.exceptions.EntityNotFoundException;
import com.acmebank.accountmanager.exceptions.TransferFailedException;
import com.acmebank.accountmanager.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/1.0/accountmanager")
public class AccountManagerRestController {
    private static final Logger log = LoggerFactory.getLogger(AccountManagerRestController.class);
    private static final String HEADER_ACCOUNT_NUMBER = "Account-Number";

    @Autowired
    private AccountService accountService;

    @GetMapping(value = "/balance",
            produces = {
                MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE,
            })
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BalanceDTO getBalance(@RequestHeader(value = HEADER_ACCOUNT_NUMBER) final String accountNumber) throws EntityNotFoundException {
        log.info("start getting balance for " + accountNumber);
        BalanceDTO balanceDTO = this.accountService.getBalance(accountNumber);
        return balanceDTO;
    }

    @PostMapping(value = "/transfer",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
            },
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
            })
    @ResponseStatus(HttpStatus.CREATED)
    public BalanceDTO transferToAccount(@RequestHeader(value = HEADER_ACCOUNT_NUMBER) final String accountNumber, @RequestBody @Valid TransferPayloadDTO payloadDto) throws EntityNotFoundException, TransferFailedException {
        log.info("start transferring     for " + accountNumber);
        BalanceDTO balanceDTO = this.accountService.transferTo(accountNumber, payloadDto);
        return balanceDTO;
    }
}
