package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.entity.Transaction;
import com.openclassrooms.paymybuddy.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    //Test GET : happy path retourne liste des transactions faites et reçues
    @Test
    void transferPage_ShouldReturnTransactions() throws Exception {

        Transaction transaction = new Transaction();

        when(transactionService.getSentTransactions("user@mail.com")).thenReturn(List.of(transaction));

        when(transactionService.getReceivedTransactions("user@mail.com")).thenReturn(List.of(transaction));

        mockMvc.perform(get("/transfer").principal(()->"user@mail.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("transfer"))
                .andExpect(model().attributeExists("sentTransactions"))
                .andExpect(model().attributeExists("receivedTransactions"));

        verify(transactionService).getSentTransactions("user@mail.com");
        verify(transactionService).getReceivedTransactions("user@mail.com");
    }

    //Test POST: Happy path transaction réussie
    @Test
    void transfer_shouldSendMoneyAndRedirectToTransferPage() throws Exception{

        mockMvc.perform(post("/transfer")
                .param("receiverEmail", "friend@mail.com")
                .param("description", "test")
                .param("amount", "10")
                .principal(()->"user@mail.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transfer"));

        verify(transactionService).sendMoney(
                "user@mail.com",
                "friend@mail.com",
                "test",
                new BigDecimal("10"));
    }

    //Test POST: erreur lorsque le montant n'est pas strictement positif
    @Test
    void transfer_whenAmountNotStrictlyPositive_shouldReturnErrorPage() throws Exception{

        doThrow(new IllegalArgumentException("Amount must be strictly positive"))
                .when(transactionService)
                .sendMoney(any(), any(), any(), any());

        mockMvc.perform(post("/transfer")
                .param("receiverEmail", "friend@mail.com")
                .param("description", "test")
                .param("amount", "0")
                .principal(()->"user@mail.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("error"));

        verify(transactionService).sendMoney(
                "user@mail.com",
                "friend@mail.com",
                "test",
                new BigDecimal("0")
        );
    }


}
