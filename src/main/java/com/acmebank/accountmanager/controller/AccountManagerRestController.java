package com.acmebank.accountmanager.controller;

import com.acmebank.accountmanager.dto.BalanceDTO;
import com.acmebank.accountmanager.dto.TransferPayloadDTO;
import com.acmebank.accountmanager.model.Account;
import com.acmebank.accountmanager.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accountmanager")
public class AccountManagerRestController {
    private static final String HEADER_ACCOUNT_NUMBER = "Account-Number";

    @Autowired
    private AccountService accountService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(value = "/balance")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BalanceDTO getBalance(@RequestHeader(HEADER_ACCOUNT_NUMBER) String accountNumber) {
        if (accountNumber.length() == 0) {

        }
        Account account = this.accountService.getAccount(accountNumber);
        return modelMapper.map(account, BalanceDTO.class);
    }

    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.OK)
    public BalanceDTO transferToAccount(@RequestHeader(HEADER_ACCOUNT_NUMBER) String accountNumber, @RequestBody TransferPayloadDTO payload, HttpServletRequest request, HttpServletResponse response) {
        Account account = this.accountService.getAccount(accountNumber);
        return modelMapper.map(account, BalanceDTO.class);
    }
}
