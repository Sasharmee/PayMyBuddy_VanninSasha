package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.entity.User;
import com.openclassrooms.paymybuddy.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profil(Model model, Principal principal){
        String userEmail = principal.getName();

        User user = userService.findByEmail(userEmail).orElseThrow(()->new IllegalArgumentException("User not found"));

        model.addAttribute("user", user);

        return "profile";
    }

    @PostMapping("/update_profile")
    public String updateProfile(String username, String password, Principal principal){

        String userEmail = principal.getName();

        userService.updateUserInformations(username, userEmail, password);

        return "redirect:/profile";
    }
}
