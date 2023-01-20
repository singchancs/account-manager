package com.acmebank.accountmanager.controller;

import com.acmebank.accountmanager.dto.BalanceDTO;
import com.acmebank.accountmanager.dto.TransferPayloadDTO;
import com.acmebank.accountmanager.dto.TransferStatusDTO;
import com.acmebank.accountmanager.exceptions.EntityNotFoundException;
import com.acmebank.accountmanager.exceptions.TransferFailedException;
import com.acmebank.accountmanager.service.AccountService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @ApiOperation(value = "Returns the current balance of your ACME bank account.",
            notes = "ACME bank account number should be provided in order to get the current balance",
            httpMethod = "GET",
            response = BalanceDTO.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid parameters supplied"),
            @ApiResponse(code = 404, message = "Account not found"),
    })
    @GetMapping(value = "/balance",
            produces = {
                MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE,
            })
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BalanceDTO getBalance(@ApiParam(name = HEADER_ACCOUNT_NUMBER, value = "ACME Bank Account Number", required = true) @RequestHeader(value = HEADER_ACCOUNT_NUMBER) final String accountNumber) throws EntityNotFoundException {
        log.info("start getting balance for " + accountNumber);
        BalanceDTO balanceDTO = this.accountService.getBalance(accountNumber);
        return balanceDTO;
    }

    @ApiOperation(value = "Creates a new transfer and returns its details.",
            notes = "ACME bank account number should be provided to create a transfer",
            httpMethod = "POST",
            response = TransferStatusDTO.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid parameters supplied"),
            @ApiResponse(code = 404, message = "Account not found"),
            @ApiResponse(code = 500, message = "Transfer failed")
    })
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
    public TransferStatusDTO transferToAccount(@ApiParam(name = HEADER_ACCOUNT_NUMBER, value = "ACME Bank Account Number", required = true) @RequestHeader(value = HEADER_ACCOUNT_NUMBER) final String accountNumber, @RequestBody @Valid TransferPayloadDTO payloadDto) throws EntityNotFoundException, TransferFailedException {
        log.info("start transferring for " + accountNumber);
        TransferStatusDTO transferStatusDTO = this.accountService.transferTo(accountNumber, payloadDto);
        return transferStatusDTO;
    }
}
