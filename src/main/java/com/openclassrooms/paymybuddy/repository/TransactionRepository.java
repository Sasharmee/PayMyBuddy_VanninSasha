package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.entity.Transaction;
import com.openclassrooms.paymybuddy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *Repository permettant l'accès aux données des transactions.
 * <p>
 * Fournit les méthodes pour récupérer les transactions envoyées et reçues par un utilisateur.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    /**
     * Récupère la liste des transactions envoyées par un utilisateur.
     *
     * @param sender expéditeur de la transaction
     * @return liste des transactions envoyées
     */
    List<Transaction> findBySender(User sender);

    /**
     * Récupère la liste des transactions reçues par un utilisateur.
     *
     * @param receiver destinataire de la transaction
     * @return liste des transactions reçues
     */
    List<Transaction> findByReceiver(User receiver);

}
