package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.dto.AddFriendDTO;
import com.openclassrooms.paymybuddy.entity.User;
import com.openclassrooms.paymybuddy.service.UserRelationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

/**
 * Contrôleur chargé de la gestion des relations des utilisateurs.
 * <p>
 * Permet d'afficher la liste des amis de l'utilisateur authentifié,
 * ainsi qu'ajouter de nouvelles relations
 */
@Controller
public class UserRelationController {

    private final UserRelationService userRelationService;

    /**
     * Constructeur du contrôleur des relations des utilisateurs.
     *
     * @param userRelationService service contenant la logique métier associé aux relations des utilisateurs
     */
    public UserRelationController(UserRelationService userRelationService) {
        this.userRelationService = userRelationService;
    }

    /**
     * Affichage la liste des amis de l'utilisateur authentifié.
     * <p>
     * Récupère les relations existantes de l'utilisateur
     *
     * @param model objet permettant de transmettre les données à la vue
     * @param principal représente l'utilisateur authentifié
     * @return le nom de la vue "friends"
     */
    @GetMapping("/friends")
    public String getFriends(Model model, Principal principal){
        String userEmail = principal.getName();

        List<User> friends = userRelationService.getFriends(userEmail);

        model.addAttribute("friends", friends);
        model.addAttribute("activePage", "friends");

        return "friends";
    }

    /**
     * Ajoute un nouvel ami à l'utilisateur.
     * <p>
     * Utilise l'email fourni pour créer une nouvelle relation entre deux utilisateurs.
     *
     * @param dto objet contenant les informations de l'ami à ajouter
     * @param principal représente l'utilisateur authentifié
     * @return une redirection vers la page "friends" après l'ajout de la relation
     */
    @PostMapping("/add_friends")
    public String addFriend(AddFriendDTO dto, Principal principal){
        String userEmail = principal.getName();

        userRelationService.addFriend(userEmail, dto.getFriendEmail());

        return "redirect:/friends";
    }
}
