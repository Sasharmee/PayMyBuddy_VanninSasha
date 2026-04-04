package com.openclassrooms.paymybuddy.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant un utilisateur de l'application Pay My Buddy.
 * <p>
 *Un utilisateur possède des informations personnelles, une liste de relation et l'historique de ses transactions.
 */
@Entity
@Table(name = "user")
public class User {

    /**
     * Identifiant unique de l'utilisateur.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    /**
     * Nom de l'utilisateur unique.
     */
    @Column(length = 50,unique = true, nullable = false)
    private String username;

    /**
     * Adresse mail unique de l'utilisateur.
     */
    @Column(length = 50,unique = true, nullable = false)
    private String email;

    /**
     * Mot de passe de l'utilisateur.
     */
    @Column(length = 100, nullable = false)
    private String password;

    /**
     * Solde du compte de l'utilisateur
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal balance;

    /**
     * Liste des amis de l'utilisateur.
     * <p>
     * Relation ManyToMany représentant les connexions entre utilisateurs.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_relation",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_friend")
    )
    private List<User> friends = new ArrayList<>();

    /**
     * Liste des transactions envoyées par l'utilisateur.
     */
    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    private List<Transaction> sentTransactions = new ArrayList<>();

    /**
     * Liste des transactions reçues par l'utilisateur.
     */
    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY)
    private List<Transaction> receivedTransactions = new ArrayList<>();

    /**
     * Constructeur par défaut.
     */
    public User() {

    }

    /**
     * Constructeur permettant de créer l'utilisateur.
     *
     * @param username nom de l'utilisateur
     * @param email adresse email de l'utilisateur
     * @param password mot de passe de l'utilisateur
     * @param balance solde du compte de l'utilisateur
     */
    public User(String username, String email, String password, BigDecimal balance) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.balance = balance;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public List<User> getFriends() {
        return friends;
    }

    public List<Transaction> getSentTransactions() {
        return sentTransactions;
    }

    public List<Transaction> getReceivedTransactions() {
        return receivedTransactions;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public void setSentTransactions(List<Transaction> sentTransactions) {
        this.sentTransactions = sentTransactions;
    }

    public void setReceivedTransactions(List<Transaction> receivedTransactions) {
        this.receivedTransactions = receivedTransactions;
    }
}

