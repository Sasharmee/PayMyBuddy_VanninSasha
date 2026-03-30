package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.entity.User;

import java.util.List;

/**
 * Interface définissant les opérations de gestion de relation des utilisateurs.
 * <p>
 * Permet de créer des relations entre utilisateurs et de récupérer la liste des relations (liste d'amis)
 *
 */
public interface UserRelationInterface {

    /**
     * Récupération de liste des relations d'un utilisateur.
     *
     * @param userEmail email de l'utilisateur
     * @return liste d'amis de l'utilisateur
     */
    List<User> getFriends (String userEmail);

    /**
     * Ajout d'une nouvelle relation.
     *
     * @param userEmail email de l'utilisateur
     * @param friendEmail email de l'ami
     */
    public void addFriend(String userEmail, String friendEmail);
}
