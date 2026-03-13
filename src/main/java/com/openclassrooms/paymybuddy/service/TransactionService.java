package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.entity.Transaction;
import com.openclassrooms.paymybuddy.entity.User;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService implements TransactionServiceInterface{

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public void sendMoney(String senderEmail, String receiverEmail, String description, BigDecimal amount) {

        User sender = userRepository.findByEmail(senderEmail).orElseThrow(()->new IllegalArgumentException("User not found"));
        User receiver = userRepository.findByEmail(receiverEmail).orElseThrow(()->new IllegalArgumentException("User not found"));

        if (sender.getId().equals(receiver.getId())){
            throw  new IllegalArgumentException("You cannot send to yourself");}

        if (!sender.getFriends().contains(receiver)){
            throw new IllegalArgumentException("You must be friend");}

        if (amount.compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalArgumentException("Amount must be strictly positive");}

        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setDescription(description);
        transaction.setAmount(amount);
        transaction.setCreatedAt(LocalDateTime.now());

        transactionRepository.save(transaction);

    }

    @Override
    public List<Transaction> getSentTransactions(String userMail) {
        User user = userRepository.findByEmail(userMail)
                .orElseThrow(()->new IllegalArgumentException("User not found"));

        return transactionRepository.findBySender(user);
    }

    @Override
    public List<Transaction> getReceivedTransactions(String userMail) {
        User user = userRepository.findByEmail(userMail)
                .orElseThrow(()->new IllegalArgumentException("User not found"));
        return transactionRepository.findByReceiver(user);
    }
}
