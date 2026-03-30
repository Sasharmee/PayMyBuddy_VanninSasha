package com.openclassrooms.paymybuddy.dto;

/**
 * Objet de transfert des données utilisé pour l'inscription d'un utilisateur.
 * <p>
 * Contient les informations nécessaires à la création d'un compte :
 * nom d'utilisateur, email et mot de passe
 *
 */
public class RegisterDTO {

    private String username;

    private String email;

    private String password;

    /**
     * Constructeur par défaut.
     */
    public RegisterDTO() {
    }

    /**
     * Récupère le nom d'utilisateur.
     *
     * @return le nom d'utilisateur
     */
    public String getUsername() {
        return username;
    }

    /**
     * Récupère l'email de l'utilisateur.
     *
     * @return l'email de l'utilisateur
     */
    public String getEmail() {
        return email;
    }

    /**
     * Récupère le mot de passe de l'utilisateur.
     *
     * @return le mot de passe de l'utilisateur
     */
    public String getPassword() {
        return password;
    }

    /**
     * Définit le nom de l'utilisateur.
     *
     * @param username nom de l'utilisateur
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Définit l'email de l'utilisateur.
     *
     * @param email email de l'utilisateur
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Définit le mot de passe de l'utilisateur.
     *
     * @param password mot de passe de l'utilisateur
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
