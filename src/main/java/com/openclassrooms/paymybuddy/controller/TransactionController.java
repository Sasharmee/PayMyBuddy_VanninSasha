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

@Controller
public class TransactionController {


    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/transfer")
    public String transferPage(Model model, Principal principal){

        String userEmail = principal.getName();

        List<Transaction> sentTransactions = transactionService.getSentTransactions(userEmail);

        List<Transaction> receivedTransactions = transactionService.getReceivedTransactions(userEmail);

        model.addAttribute("sentTransactions", sentTransactions);
        model.addAttribute("receivedTransactions", receivedTransactions);

        return "transfer";
    }

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
