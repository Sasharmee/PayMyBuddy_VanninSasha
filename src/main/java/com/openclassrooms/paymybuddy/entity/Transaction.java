package com.openclassrooms.paymybuddy.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entité représentant une transaction entre deux utilisateurs.
 * <p>
 * Une transaction est un transfert d'argent d'un utilisateur à un autre allant d'un utilisateur,
 * à un autre, ayant un montant, une description et une date de création.
 *
 */
@Entity
@Table(name = "transaction")
public class Transaction {
    /**
     * Identifiant unique de la transaction.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    /**
     * Description associée à la transaction.
     */
    @Column(length = 255, nullable = false)
    private String description;

    /**
     * Montant de la transaction.
     * <p>
     * Utilise BigDecimal afin de garantir la précision des calculs financiers.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    /**
     * Date et heure de création de la transaction.
     */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Utilisateur expéditeur de la transaction.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_sender_id")
    private User sender;

    /**
     * Utilisateur destinataire de la transaction.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_receiver_id")
    private User receiver;

    /**
     * Constructeur par défaut.
     */
    public Transaction() {
    }

    /**
     * Constructeur complet d'une transaction.
     *
     * @param description description de la transaction
     * @param amount montant de la description
     * @param createdAt date et heure de création de la transaction
     * @param sender expéditeur de la transaction
     * @param receiver destinataire de la transaction
     */
    public Transaction(String description, BigDecimal amount, LocalDateTime createdAt, User sender, User receiver) {
        this.description = description;
        this.amount = amount;
        this.createdAt = createdAt;
        this.sender = sender;
        this.receiver = receiver;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
}
