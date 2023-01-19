package com.acmebank.accountmanager.controller;

import com.acmebank.accountmanager.dto.BalanceDTO;
import com.acmebank.accountmanager.dto.TransferPayloadDTO;
import com.acmebank.accountmanager.dto.TransferResultDTO;
import com.acmebank.accountmanager.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accountmanager")
public class AccountManagerRestController {
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
    public BalanceDTO getBalance(@RequestHeader(HEADER_ACCOUNT_NUMBER) String accountNumber) throws Exception {
        if (accountNumber.length() == 0) {

        }
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
    public TransferResultDTO transferToAccount(@RequestHeader(HEADER_ACCOUNT_NUMBER) String accountNumber, @RequestBody TransferPayloadDTO payloadDto) throws Exception {
        TransferResultDTO transferResultDTO = this.accountService.transferTo(accountNumber, payloadDto);
        return transferResultDTO;
    }
}
