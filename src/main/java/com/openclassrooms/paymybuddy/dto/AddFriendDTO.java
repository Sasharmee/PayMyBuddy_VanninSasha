package com.openclassrooms.paymybuddy.dto;

/**
 * Objet de transfert des données utilisées pour ajouter un ami.
 * <p>
 * Contient l'email de l'utilisateur à ajouter.
 *
 */
public class AddFriendDTO {

    private String friendEmail;

    /**
     * Constructeur par défaut.
     */
    public AddFriendDTO() {
    }

    /**
     * Récupère l'email de l'utilisateur à ajouter.
     *
     * @return l'email de l'ami
     */
    public String getFriendEmail() {
        return friendEmail;
    }

    /**
     * Définit l'email de l'ami à ajouter.
     *
     * @param friendEmail email de l'ami
     */
    public void setFriendEmail(String friendEmail) {
        this.friendEmail = friendEmail;
    }
}
