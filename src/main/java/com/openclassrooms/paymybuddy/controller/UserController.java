package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.entity.User;
import com.openclassrooms.paymybuddy.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

/**
 * Contrôleur gérant le profil de l'utilisateur.
 * <p>
 * Permet d'afficher les différentes informations du profil de l'utilisateur authentifié,
 * ainsi que modifier ces dernières
 *
 */
@Controller
public class UserController {

    private final UserService userService;

    /**
     * Constructeur du contrôleur utilisateur.
     *
     * @param userService service contenant la logique métier des opérations liées à l'utilisateur*
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Affichage de la page profil de l'utilisateur authentifié.
     * <p>
     * Récupère les informations personnelles de l'utilisateur et les transmets à la vue afin de les afficher.
     *
     * @param model objet permettant de transmettre les données à la vue
     * @param principal représente l'utilisateur actuellement authentifié
     * @return le nom de la vue "profile
     * @throws IllegalArgumentException si l'utilisateur n'est pas trouvé
     */
    @GetMapping("/profile")
    public String profil(Model model, Principal principal){
        String userEmail = principal.getName();

        User user = userService.findByEmail(userEmail).orElseThrow(()->new IllegalArgumentException("User not found"));

        model.addAttribute("user", user);
        model.addAttribute("activePage", "profile");

        return "profile";
    }

    /**
     * Met à jour les informations de l'utilisateur.
     * <p>
     * Permet de modifier le nom de l'utilisateur et/ou son mot de passe
     *
     * @param username nouveau nom d'utilisateur
     * @param password nouveau mot de passe
     * @param principal représente l'utilisateur authentifié
     * @return redirection vers la page "profile" après la mise à jour des informations
     */
    @PostMapping("/update_profile")
    public String updateProfile(String username, String password, Principal principal){

        String userEmail = principal.getName();

        userService.updateUserInformations(username, userEmail, password);

        return "redirect:/profile";
    }
}
