package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.entity.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRelationService implements UserRelationInterface{

    private final UserRepository userRepository;

    public UserRelationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getFriends(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(()->new IllegalArgumentException("User not found"));
        return user.getFriends();
    }

    @Override
    public void addFriend(String userEmail, String friendEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(()->new IllegalArgumentException("User not found"));

        User friend = userRepository.findByEmail(friendEmail)
                .orElseThrow(()->new IllegalArgumentException("Friend not found"));

        if(user.getId().equals(friend.getId())){
            throw new IllegalArgumentException("You cannot add yourself");}

        if (user.getFriends().stream()
                .anyMatch(f -> f.getId().equals(friend.getId()))){
            throw new IllegalArgumentException("ALready friend");}

        user.getFriends().add(friend);

        userRepository.save(user);

    }
}
