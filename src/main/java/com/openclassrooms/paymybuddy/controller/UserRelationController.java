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

@Controller
public class UserRelationController {

    private final UserRelationService userRelationService;

    public UserRelationController(UserRelationService userRelationService) {
        this.userRelationService = userRelationService;
    }

    @GetMapping("/friends")
    public String getFriends(Model model, Principal principal){
        String userEmail = principal.getName();

        List<User> friends = userRelationService.getFriends(userEmail);

        model.addAttribute("friends", friends);

        return "friends";
    }

    @PostMapping("/add_friends")
    public String addFriend(AddFriendDTO dto, Principal principal){
        String userEmail = principal.getName();

        userRelationService.addFriend(userEmail, dto.getFriendEmail());

        return "redirect:/friends";
    }
}
