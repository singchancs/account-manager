package com.acmebank.accountmanager.controller;

import com.acmebank.accountmanager.dao.AccountRepository;
import com.acmebank.accountmanager.model.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountManagerRestControllerTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private AccountRepository repository;

    private void resetAccountBalance(String accountNumber, String balance) {
        Account account = repository.findByAccountNumber(accountNumber);
        account.setBalance(new BigDecimal(balance));
        repository.save(account);
    }

    private void resetBalance() {
        resetAccountBalance("12345678", "1000000");
        resetAccountBalance("88888888", "1000000");
    }

    private HttpHeaders getAccountHeader(String accountNumber) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Account-Number", accountNumber);
        return httpHeaders;
    }

    @Test
    @DisplayName("Should return balance when request is valid")
    public void shouldReturnBalanceWhenValid() throws Exception {
        this.resetBalance();

        mvc.perform(MockMvcRequestBuilders.get("/api/1.0/accountmanager/balance")
                        .accept(MediaType.APPLICATION_JSON)
                        .headers(this.getAccountHeader("12345678"))
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.currency").value("HKD"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value("1000000.0000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastUpdatedAt").isNotEmpty());
    }
    @Test
    @DisplayName("Should return 404 when account is not found")
    public void shouldReturn404WhenAccountNotFound() throws Exception {
        this.resetBalance();

        mvc.perform(MockMvcRequestBuilders.get("/api/1.0/accountmanager/balance")
                        .accept(MediaType.APPLICATION_JSON)
                        .headers(this.getAccountHeader("00000000"))
                )
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.apierror.errorCode").value("ERR_ACME_002"));;
    }

    @Test
    @DisplayName("Should transfer to account when request is valid")
    public void shouldTransferWhenValid() throws Exception {
        this.resetBalance();

        mvc.perform(MockMvcRequestBuilders.post("/api/1.0/accountmanager/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .headers(this.getAccountHeader("12345678"))
                .content("{ \"targetAccountNumber\": \"88888888\", \"transferCurrency\": \"HKD\", \"transferAmount\": \"500000.0000\"}")
        )
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currency").value("HKD"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value("500000.0000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.transferAt").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.transferTo").value("88888888"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance.currency").value("HKD"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance.balance").value("500000.0000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance.lastUpdatedAt").isNotEmpty());
    }

    @Test
    @DisplayName("Should return 404 when source account is not found")
    public void shouldReturn404WhenSourceAccountNotFound() throws Exception {
        this.resetBalance();

        mvc.perform(MockMvcRequestBuilders.post("/api/1.0/accountmanager/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .headers(this.getAccountHeader("11111111"))
                .content("{ \"targetAccountNumber\": \"88888888\", \"transferCurrency\": \"HKD\", \"transferAmount\": \"5000.0\"}")
        )
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.apierror.errorCode").value("ERR_ACME_002"));;
    }

    @Test
    @DisplayName("Should return 404 when target account is not found")
    public void shouldReturn404WhenTargetAccountNotFound() throws Exception {
        this.resetBalance();

        mvc.perform(MockMvcRequestBuilders.post("/api/1.0/accountmanager/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .headers(this.getAccountHeader("12345678"))
                .content("{ \"targetAccountNumber\": \"22222222\", \"transferCurrency\": \"HKD\", \"transferAmount\": \"5000.0\"}")
        ).andExpect(status().isNotFound())
        .andExpect(MockMvcResultMatchers.jsonPath("$.apierror.errorCode").value("ERR_ACME_002"));
    }

    @Test
    @DisplayName("Should return 500 when source account is the same as target account ")
    public void shouldReturn500WhenSourceTargetAreSame() throws Exception {
        this.resetBalance();

        mvc.perform(MockMvcRequestBuilders.post("/api/1.0/accountmanager/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .headers(this.getAccountHeader("12345678"))
                .content("{ \"targetAccountNumber\": \"12345678\", \"transferCurrency\": \"HKD\", \"transferAmount\": \"5000.0\"}")
        )
                .andExpect(status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.apierror.errorCode").value("ERR_ACME_004"));;
    }

    @Test
    @DisplayName("Should return 500 when source account balance is insufficient")
    public void shouldReturn500WhenInsufficientBalance() throws Exception {
        this.resetBalance();
        this.resetAccountBalance("12345678", "1000.0000");

        mvc.perform(MockMvcRequestBuilders.post("/api/1.0/accountmanager/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .headers(this.getAccountHeader("12345678"))
                .content("{ \"targetAccountNumber\": \"88888888\", \"transferCurrency\": \"HKD\", \"transferAmount\": \"5000.0\"}")
        )
                .andExpect(status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.apierror.errorCode").value("ERR_ACME_003"));;
    }

    @Test
    @DisplayName("Should return 500 when source account balance is zero")
    public void shouldReturn500WhenZeroBalance() throws Exception {
        this.resetBalance();
        this.resetAccountBalance("12345678", "0.0000");

        mvc.perform(MockMvcRequestBuilders.post("/api/1.0/accountmanager/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .headers(this.getAccountHeader("12345678"))
                .content("{ \"targetAccountNumber\": \"88888888\", \"transferCurrency\": \"HKD\", \"transferAmount\": \"5000.0\"}")
        )
                .andExpect(status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.apierror.errorCode").value("ERR_ACME_003"));
    }

    @Test
    @DisplayName("Should return 400 when incorrect parameters supplied")
    public void shouldReturn500WhenIncorrectParamSupplied() throws Exception {
        this.resetBalance();

        mvc.perform(MockMvcRequestBuilders.post("/api/1.0/accountmanager/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .headers(this.getAccountHeader("12345678"))
                .content("{ \"targetAccountNumber\": \"88888888\", \"transferCurrency\": \"HKD\" }")
        )
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.apierror.errorCode").value("ERR_ACME_006"));
    }
}
