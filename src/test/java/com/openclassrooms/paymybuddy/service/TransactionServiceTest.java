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

/**
 * Tests unitaires du service TransactionService.
 * <p>
 * Vérifie la logique métier liée aux transactions.
 * Utilise Mockito pour isoler le service et simuler les différentes dépendances.
 */
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

    /**
     * Initialisation des données de test.
     */
    @BeforeEach
    void setUp() {
        sender = new User("sender" , "sender@mail.com", "1234", new BigDecimal("20"));
        sender.setId(1);
        sender.setFriends(new ArrayList<>());

        receiver = new User("receiver", "receiver@mail.com", "5678", new BigDecimal("10"));
        receiver.setId(2);
        receiver.setFriends(new ArrayList<>());

        sender.getFriends().add(receiver);
    }

    /**
     * Vérifie qu'une transaction a correctement lieu et est enregistrée.
     */
    @Test
    void sendMoney_whenDataAreValid_savesTransactionAndUpdateBalances() {

        when(userRepository.findByEmail("sender@mail.com")).thenReturn(Optional.of(sender));
        when(userRepository.findByEmail("receiver@mail.com")).thenReturn(Optional.of(receiver));

        transactionService.sendMoney("sender@mail.com", "receiver@mail.com","test", new BigDecimal("5"));

        assertEquals(new BigDecimal("15"), sender.getBalance());
        assertEquals(new BigDecimal("15"), receiver.getBalance());

        verify(transactionRepository).save(any(Transaction.class));
        verify(userRepository, times(2)).save(any(User.class));
    }

    /**
     * Vérifie qu'une exception est levée lorsque l'utilisateur n'est pas trouvé.
     */
    @Test
    void sendMoney_whenUserIsNotFound_throwsException() {
        when(userRepository.findByEmail("sender@mail.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, ()->
                transactionService.sendMoney("sender@mail.com", "receiver@mail.com", "test", BigDecimal.TEN));

        assertEquals("User not found", exception.getMessage());
        verify(transactionRepository, never()).save(any());
    }

    /**
     * Vérifie qu'une exception est levée lorsque l'utilisateur essaie de se faire une transaction à lui-même.
     */
    @Test
    void sendMoney_whenUserSendToHimself_throwsException() {
        when(userRepository.findByEmail("sender@mail.com")).thenReturn(Optional.of(sender));

        Exception exception = assertThrows(IllegalArgumentException.class, ()->
                transactionService.sendMoney("sender@mail.com", "sender@mail.com", "test", BigDecimal.TEN));

        assertEquals("You cannot send to yourself", exception.getMessage());
        verify(transactionRepository, never()).save(any());
    }

    /**
     * Vérifie qu'une exception est levée lorsque les utilisateurs ne sont pas amis.
     */
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

    /**
     * Vérifie qu'une exception est levée lorsque le montant n'est pas strictement positif.
     */
    @Test
    void sendMoney_whenAmountIsNotPositive_throwsException() {
        when(userRepository.findByEmail("sender@mail.com")) .thenReturn(Optional.of(sender));
        when(userRepository.findByEmail("receiver@mail.com")).thenReturn(Optional.of(receiver));

        Exception exception = assertThrows(IllegalArgumentException.class, ()->
                transactionService.sendMoney("sender@mail.com", "receiver@mail.com", "test", BigDecimal.ZERO));

        assertEquals("Amount must be strictly positive", exception.getMessage());
        verify(transactionRepository, never()).save(any());
    }

    /**
     * Vérifie qu'une exception est levée lorsque le solde du compte de l'expéditeur n'est pas suffisant
     */
    @Test
    void sendMoney_whenBalanceIsNotSufficient_throwsException() {

        sender.setBalance(new BigDecimal("5"));

        when(userRepository.findByEmail("sender@mail.com")).thenReturn(Optional.of(sender));
        when(userRepository.findByEmail("receiver@mail.com")).thenReturn(Optional.of(receiver));

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                transactionService.sendMoney("sender@mail.com", "receiver@mail.com", "test", new BigDecimal("10"))
        );

        assertEquals("Insufficient balance", exception.getMessage());
        verify(transactionRepository, never()).save(any());

    }

    /**
     * Vérifie que la liste des transactions effectuées est retournée.
     */
    @Test
    void getSentTransactions_whenUserIsValid_shouldReturnListOfSentTransactions() {
        List<Transaction> transactions = List.of(new Transaction());
        when(userRepository.findByEmail("sender@mail.com")) .thenReturn(Optional.of(sender));
        when(transactionRepository.findBySender(sender)).thenReturn(transactions);

        List<Transaction> result = transactionService.getSentTransactions("sender@mail.com");

        assertEquals(1, result.size());

    }

    /**
     * Vérifie qu'une exception est levée lorsque l'utilisateur n'est pas retrouvé.
     */
    @Test
    void getSentTransactions_whenUserIsNotFound_throwsException() {
        when(userRepository.findByEmail("sender@mail.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, ()->
                transactionService.getSentTransactions("sender@mail.com"));

        assertEquals("User not found", exception.getMessage());


    }

    /**
     * Vérifie que les transactions reçues sont correctement retournées.
     */
    @Test
    void getReceivedTransactions_whenUserIsValid_shouldReturnListOfReceivedTransactions() {
        List<Transaction> transactions = List.of(new Transaction());
        when(userRepository.findByEmail("receiver@mail.com")) .thenReturn(Optional.of(receiver));
        when(transactionRepository.findByReceiver(receiver)).thenReturn(transactions);

        List<Transaction> result = transactionService.getReceivedTransactions("receiver@mail.com");

        assertEquals(1, result.size());
    }

    /**
     * Vérifie qu'une exception est levée lorsque l'utilisateur n'est pas retrouvé.
     */
    @Test
    void getReceivedTransactions_whenUserIsNotFound_throwsException() {
        when(userRepository.findByEmail("receiver@mail.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, ()->
                transactionService.getReceivedTransactions("receiver@mail.com"));

        assertEquals("User not found", exception.getMessage());


    }
}
