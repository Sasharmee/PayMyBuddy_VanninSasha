package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.entity.User;
import jakarta.transaction.Transactional;

/**
 * Interface définissant les opérations de gestion des comptes des utilisateurs.
 * <p>
 * Elle gère l'inscription et la mise à jour des informations des utilisateurs
 *
 */
public interface UserAccountManagementInterface {

    /**
     * Inscription d'un nouvel utilisateur.
     *
     * @param username nom d'utilisateur
     * @param email email de l'utilisateur
     * @param password mot de passe de l'utilisateur (chiffré par la suite)
     * @return l'utilisateur créé
     */
    User registerUser(String username, String email, String password);

    /**
     * Mise à jour des informations de l'utilisateur.
     *
     * @param username nom d'utilisateur
     * @param email email de l'utilisateur
     * @param password mot de passe de l'utilisateur (chiffré par la suite)
     * @return l'utilisateur mis à jour
     */
    User updateUserInformations(String username, String email, String password);
}
