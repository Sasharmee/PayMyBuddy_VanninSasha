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

/**
 * Service gérant la logique métier des transactions.
 * <p>
 * Permet d'effectuer des transactions entre utilisateurs et de
 * consulter l'historique des transactions reçues et effectuées.
 *
 */
@Service
public class TransactionService implements TransactionServiceInterface{

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Constructeur du service de transactions.
     *
     * @param userRepository repository des utilisateurs
     * @param transactionRepository repository des transactions
     */
    public TransactionService(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Transfert d'argent d'un utilisateur à un autre.
     * <p>
     * Vérifie plusieurs règles métier :
     * <ul>
     *     <li>Les utilisateurs existent</li>
     *     <li>L'expéditeur et le destinataire sont différents</li>
     *     <li>Les utilisateurs sont liés (amis)</li>
     *     <li>Le montant est strictement positif</li>
     *     <li>L'expéditeur doit disposer d'un solde suffisant</li>
     * </ul>
     *
     * @param senderEmail email de l'expéditeur
     * @param receiverEmail email du destinataire
     * @param description description de la transaction
     * @param amount montant de la transaction
     */
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

        if (sender.getBalance().compareTo(amount)<0) {
            throw  new IllegalArgumentException("Insufficient balance");
        }

        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setDescription(description);
        transaction.setAmount(amount);
        transaction.setCreatedAt(LocalDateTime.now());

        transactionRepository.save(transaction);

        userRepository.save(sender);
        userRepository.save(receiver);

    }

    /**
     * Récupération des transactions effectuées par l'utilisateur.
     *
     * @param userMail email de l'utilisateur
     * @return liste des transactions effectuées
     * @throws IllegalArgumentException si l'utilisateur renseigné n'est pas trouvé
     */
    @Override
    public List<Transaction> getSentTransactions(String userMail) {
        User user = userRepository.findByEmail(userMail)
                .orElseThrow(()->new IllegalArgumentException("User not found"));

        return transactionRepository.findBySender(user);
    }

    /**
     * Récupération des transactions reçues par l'utilisateur.
     *
     * @param userMail email de l'utilisateur
     * @return liste des transactions reçues
     * @throws IllegalArgumentException si l'utilisateur renseigné n'est pas trouvé
     */
    @Override
    public List<Transaction> getReceivedTransactions(String userMail) {
        User user = userRepository.findByEmail(userMail)
                .orElseThrow(()->new IllegalArgumentException("User not found"));
        return transactionRepository.findByReceiver(user);
    }
}
