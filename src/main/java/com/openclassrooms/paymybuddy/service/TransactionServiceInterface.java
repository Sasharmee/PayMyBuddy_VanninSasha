package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.entity.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionServiceInterface {

    void sendMoney(String senderEmail, String receiverEmail, String description, BigDecimal amount);

    List<Transaction> getSentTransactions(String userMail);

    List<Transaction> getReceivedTransactions(String userMail);
}
