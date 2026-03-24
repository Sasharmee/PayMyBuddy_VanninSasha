package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.entity.Transaction;
import com.openclassrooms.paymybuddy.entity.User;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    TransactionRepository transactionRepository;

    @InjectMocks
    TransactionService transactionService;

    private User sender;
    private User receiver;

    @BeforeEach
    void setUp() {
        sender = new User("sender" , "sender@mail.com", "1234");
        sender.setId(1);
        sender.setFriends(new ArrayList<>());

        receiver = new User("receiver", "receiver@mail.com", "5678");
        receiver.setId(2);
        receiver.setFriends(new ArrayList<>());

        sender.getFriends().add(receiver);
    }

    //Test happy path de la transaction
    @Test
    void sendMoney_whenDataAreValid_savesTransaction() {
        when(userRepository.findByEmail("sender@mail.com")).thenReturn(Optional.of(sender));
        when(userRepository.findByEmail("receiver@mail.com")).thenReturn(Optional.of(receiver));

        transactionService.sendMoney("sender@mail.com", "receiver@mail.com","test", BigDecimal.TEN);

        verify(transactionRepository).save(any(Transaction.class));
    }

    //Test lorsque l'utilisateur n'est pas retrouvé
    @Test
    void sendMoney_whenUserIsNotFound_throwsException() {
        when(userRepository.findByEmail("sender@mail.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, ()->
                transactionService.sendMoney("sender@mail.com", "receiver@mail.com", "test", BigDecimal.TEN));

        assertEquals("User not found", exception.getMessage());
        verify(transactionRepository, never()).save(any());
    }

    //Test lorsque l'utilisateur essaie de s'envoyer de l'argent à lui-même
    @Test
    void sendMoney_whenUserSendToHimself_throwsException() {
        when(userRepository.findByEmail("sender@mail.com")).thenReturn(Optional.of(sender));

        Exception exception = assertThrows(IllegalArgumentException.class, ()->
                transactionService.sendMoney("sender@mail.com", "sender@mail.com", "test", BigDecimal.TEN));

        assertEquals("You cannot send to yourself", exception.getMessage());
        verify(transactionRepository, never()).save(any());
    }

    //Test lorsque les utilisateurs ne sont pas amis
    @Test
    void sendMoney_whenUsersAreNotFriends_throwsException() {
        sender.getFriends().clear();
        when(userRepository.findByEmail("sender@mail.com")) .thenReturn(Optional.of(sender));
        when(userRepository.findByEmail("receiver@mail.com")).thenReturn(Optional.of(receiver));

        Exception exception = assertThrows(IllegalArgumentException.class, ()->
                transactionService.sendMoney("sender@mail.com", "receiver@mail.com", "test", BigDecimal.TEN));

        assertEquals("You must be friend", exception.getMessage());
        verify(transactionRepository, never()).save(any());

    }

    //Test lorsque le montant envoyé est négatif ou nul
    @Test
    void sendMoney_whenAmountIsNotPositive_throwsException() {
        when(userRepository.findByEmail("sender@mail.com")) .thenReturn(Optional.of(sender));
        when(userRepository.findByEmail("receiver@mail.com")).thenReturn(Optional.of(receiver));

        Exception exception = assertThrows(IllegalArgumentException.class, ()->
                transactionService.sendMoney("sender@mail.com", "receiver@mail.com", "test", BigDecimal.ZERO));

        assertEquals("Amount must be strictly positive", exception.getMessage());
        verify(transactionRepository, never()).save(any());
    }

    //Test happy path récupération liste argent des transactions envoyées
    @Test
    void getSentTransactions_whenUserIsValid_shouldReturnListOfSentTransactions() {
        List<Transaction> transactions = List.of(new Transaction());
        when(userRepository.findByEmail("sender@mail.com")) .thenReturn(Optional.of(sender));
        when(transactionRepository.findBySender(sender)).thenReturn(transactions);

        List<Transaction> result = transactionService.getSentTransactions("sender@mail.com");

        assertEquals(1, result.size());

    }

    //Test lorsque l'utilisateur n'est pas retrouvé
    @Test
    void getSentTransactions_whenUserIsNotFound_throwsException() {
        when(userRepository.findByEmail("sender@mail.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, ()->
                transactionService.getSentTransactions("sender@mail.com"));

        assertEquals("User not found", exception.getMessage());


    }

    //Test happy path récupération des transactions reçues
    @Test
    void getReceivedTransactions_whenUserIsValid_shouldReturnListOfReceivedTransactions() {
        List<Transaction> transactions = List.of(new Transaction());
        when(userRepository.findByEmail("receiver@mail.com")) .thenReturn(Optional.of(receiver));
        when(transactionRepository.findByReceiver(receiver)).thenReturn(transactions);

        List<Transaction> result = transactionService.getReceivedTransactions("receiver@mail.com");

        assertEquals(1, result.size());
    }

    //Test lorsque l'utilisateur n'est pas retrouvé
    @Test
    void getReceivedTransactions_whenUserIsNotFound_throwsException() {
        when(userRepository.findByEmail("receiver@mail.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, ()->
                transactionService.getReceivedTransactions("receiver@mail.com"));

        assertEquals("User not found", exception.getMessage());


    }
}
