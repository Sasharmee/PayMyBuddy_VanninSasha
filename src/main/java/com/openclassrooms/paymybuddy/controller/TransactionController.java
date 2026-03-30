package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.dto.TransferDTO;
import com.openclassrooms.paymybuddy.entity.Transaction;
import com.openclassrooms.paymybuddy.service.TransactionService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

/**
 * Contrôleur gérant les transactions entre utilisateurs.
 *
 * Il permet d'afficher l'historique des transactions envoyées et reçues,
 * ainsi qu'effectuer un transfert entre utilisateurs.
 */
@Controller
public class TransactionController {


    private final TransactionService transactionService;

    /**
     * Constructeur du contrôleur des transactions.
     *
     * @param transactionService service gérant la logique métier des transactions.
     */
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Affichage de la page des transactions.
     * <p>
     * Récupère les transactions effectuées et reçues par l'utilisateur et les ajoute au modèle pour affichage.
     *
     *
     * @param model objet permettant de transmettre les données à la vue
     * @param principal représente l'utilisateur authentifié
     * @return le nom de la vue "transfer"
     */
    @GetMapping("/transfer")
    public String transferPage(Model model, Principal principal){

        String userEmail = principal.getName();

        List<Transaction> sentTransactions = transactionService.getSentTransactions(userEmail);

        List<Transaction> receivedTransactions = transactionService.getReceivedTransactions(userEmail);

        model.addAttribute("sentTransactions", sentTransactions);
        model.addAttribute("receivedTransactions", receivedTransactions);
        model.addAttribute("activePage", "transfer");

        return "transfer";
    }

    /**
     * Gestion de transfert d'argent entre deux utilisateurs.
     *
     * Récupère les informations du formulaire d'envoi et l'effectue
     *
     * @param dto objet contenant les informations du transfert
     * @param principal représente l'utilisateur authentifié
     * @return redirection vers la page de transactions après la réalisation de l'opération
     */
    @PostMapping("/transfer")
    public String transfer(TransferDTO dto, Principal principal) {

        String senderEmail = principal.getName();

        transactionService.sendMoney(senderEmail,
                dto.getReceiverEmail(),
                dto.getDescription(),
                dto.getAmount());

        return "redirect:/transfer";
    }
}
