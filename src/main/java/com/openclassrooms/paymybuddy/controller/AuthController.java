package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.dto.RegisterDTO;
import com.openclassrooms.paymybuddy.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *Contrôleur responsable de l'authentification des utilisateurs
 * <p>
 * Cela inclut l'affichage des pages de connexion et d'enregistrement,
 * ainsi que les requêtes liées à la connexion et à l'enregistrement.
 */
@Controller
public class AuthController {

    private final UserService userService;

    /**
     *Constructeur du contrôleur d'authentification des utilisateurs
     *
     * @param userService Service permettant la gestion des opérations d'authentification.
     */
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Affichage de la page de connexion
     *
     * @return le nom de la vue "login"
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * Traite la requête liée au formulaire de connexion (démonstration vérification mot de passe)
     *
     * @param email email de l'utilisateur
     * @param password mot de passe de l'utilisateur
     * @return le nom de la vue "test-login"
     */
    @PostMapping("/test-login")
    public String testLogin(String email, String password) {

        userService.login(email, password);

        return "redirect:/profile";
    }

    /**
     * Affichage de la page de connexion
     *
     * @return le nom de la vue "register"
     */
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    /**
     * Traite la requête liée au formulaire d'inscription.
     * <p>
     * Création d'un nouvel utilisateur via les paramètres fournis par l'utilisateur.
     *
     * @param dto objet contenant les paramètres nécessaires à l'inscription à Pay My Buddy.
     * @return une redirection vers la page de connexion suite à l'inscription.
     */
    @PostMapping("/register")
    public String registerUser(RegisterDTO dto) {

        userService.registerUser(dto.getUsername(), dto.getEmail(), dto.getPassword());

        return "redirect:/login";
    }
}
