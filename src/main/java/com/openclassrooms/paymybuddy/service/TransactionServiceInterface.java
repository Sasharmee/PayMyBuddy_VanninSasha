package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.entity.Transaction;

import java.math.BigDecimal;
import java.util.List;

/**
 * Interface définissant les opérations liées aux transactions.
 * <p>
 * Permet d'abstraire la logique métier des transactions et de faciliter
 * les tests ainsi que la maintenance du code.
 */
public interface TransactionServiceInterface {

    /**
     * Transaction d'un utilisateur à un autre.
     *
     * @param senderEmail email de l'expéditeur
     * @param receiverEmail email du destinataire
     * @param description description de la transaction
     * @param amount montant de la transaction
     */
    void sendMoney(String senderEmail, String receiverEmail, String description, BigDecimal amount);

    /**
     * Récupération de l'historique des transactions effectuées.
     *
     * @param userMail email de l'utilisateur
     * @return liste des transactions effectuées
     */
    List<Transaction> getSentTransactions(String userMail);

    /**
     * Récupération de l'historique des transactions reçues.
     *
     * @param userMail email de l'utilisateur
     * @return liste des transactions reçues
     */
    List<Transaction> getReceivedTransactions(String userMail);
}
