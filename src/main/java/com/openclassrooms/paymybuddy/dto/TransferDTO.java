package com.openclassrooms.paymybuddy.dto;

import java.math.BigDecimal;

/**
 * Objet de transfert de données utilisé pour effectuer des transferts entre utilisateurs.
 *
 * Contient les informations nécessaires à la transaction :
 * email du destinataire, montant et description.
 *
 */
public class TransferDTO {

    private String receiverEmail;

    private BigDecimal amount;

    private String description;

    /**
     * Constructeur par défaut.
     */
    public TransferDTO() {
    }

    /**
     * Récupère l'email du destinataire du transfert.
     *
     * @return email du destinataire
     */
    public String getReceiverEmail() {
        return receiverEmail;
    }

    /**
     * Récupère le montant de la transaction.
     *
     * @return montant de la transaction
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Récupère la description de la transaction
     *
     * @return la description de la transaction
     */
    public String getDescription() {
        return description;
    }

    /**
     * Définit l'email du destinataire de la transaction.
     *
     * @param receiverEmail email du destinataire de la transaction
     */
    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    /**
     * Définit le montant de la transaction.
     *
     * @param amount montant de la transaction
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Définit la description de la transaction.
     *
     * @param description description de la transaction
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
