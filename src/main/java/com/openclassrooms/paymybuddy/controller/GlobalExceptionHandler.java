package com.openclassrooms.paymybuddy.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Gestionnaire global des exceptions de l'application.
 * <p>
 * Elle intercepte les exceptions levées par les contrôleurs et affiche une page d'erreur spécifique.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Gère les exceptions de type :"IllegalArgumentException".
     * <p>
     * Affiche une page d'erreur avec un message spécifique lié à l'erreur.
     *
     * @param exception exception levée lors de la requête
     * @param model objet permettant de transmettre les données à la vue
     * @return le nom de la vue "error"
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException exception, Model model){

        model.addAttribute("error", exception.getMessage());

        return "error";
    }
}
